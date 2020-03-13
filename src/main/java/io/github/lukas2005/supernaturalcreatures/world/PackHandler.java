package io.github.lukas2005.supernaturalcreatures.world;

import io.github.lukas2005.supernaturalcreatures.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.entity.ModEntities;
import io.github.lukas2005.supernaturalcreatures.network.CapabilitySyncMessage;
import io.github.lukas2005.supernaturalcreatures.network.ISyncable;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PackHandler implements IPackHandler, ISyncable {

    @CapabilityInject(IPackHandler.class)
    public static final Capability<IPackHandler> CAP = null;
    public static final ResourceLocation CAP_KEY = new ResourceLocation(Reference.MOD_ID, "pack_handler");

    public World world;

    public HashMap<ChunkPos, Pack> packChunks = new HashMap<>();
    public ArrayList<Pack> allPacks = new ArrayList<>();

    public ArrayList<ChunkPos> closed = new ArrayList<>();

    private volatile LinkedList<Chunk> chunkQueue = new LinkedList<>();

    private boolean isActive = false;
    private volatile Thread thread;

    public PackHandler(World world) {
        this.world = world;
    }

    public static PackHandler forWorld(World world) {
        return (PackHandler) world.getCapability(CAP, null).orElseThrow(()->new IllegalStateException("World doesn't have capability!"));
    }

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(IPackHandler.class, new IPackHandler.Storage(), IPackHandler.Impl::new);
        ModCapabilities.CAPABILITY_MAP.put(CAP_KEY, CAP);
    }

    public static ICapabilityProvider createNewCapability(final World world) {
        return new ICapabilitySerializable<CompoundNBT>() {

            final PackHandler inst = new PackHandler(world);
            final LazyOptional<IPackHandler> opt = LazyOptional.of(() -> inst);

            @Override
            public void deserializeNBT(CompoundNBT nbt) {
                CAP.getStorage().readNBT(CAP, inst, null, nbt);
            }

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
                return CAP.orEmpty(capability, opt);
            }

            @Override
            public CompoundNBT serializeNBT() {
                return (CompoundNBT) CAP.getStorage().writeNBT(CAP, inst, null);
            }
        };
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public ArrayList<Pack> getAllPacks() {
        return allPacks;
    }

    @Override
    public HashMap<ChunkPos, Pack> getPackChunks() {
        return packChunks;
    }

    @Override
    public Pack newPack(Chunk mainChunk) {
        Pack pack = new Pack(mainChunk);

        packChunks.put(mainChunk.getPos(), pack);
        allPacks.add(pack);

        return pack;
    }

    @Override
    public Pack getPackAt(ChunkPos pos) {
        return packChunks.get(pos);
    }

    @Override
    public void growPack(Pack pack, Chunk chunk) {
        if (pack.territoryList.size()+1 <= pack.maxSize) {
            pack.territoryList.add(chunk);
            packChunks.put(chunk.getPos(), pack);
        }
    }

    @Override
    public boolean isOccupied(Chunk chunk) {
        return isOccupied(chunk.getPos());
    }

    @Override
    public boolean isOccupied(ChunkPos pos) {
        return packChunks.containsKey(pos);
    }

    @Override
    public boolean isAllFree(ChunkPos[] poss) {
        for (ChunkPos pos : poss) {
            if (isOccupied(pos)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isRadiusClear(ChunkPos center, int r) {
        for (int x = center.x-r; x <= center.x+r; x++) {
            for (int z = center.z-r; z <= center.z+r; z++) {
                if (isOccupied(new ChunkPos(x,z))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void sync() {
        if (!world.isRemote) {
             NetworkManager.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> getWorld().getDimension().getType()), new CapabilitySyncMessage(this, CAP, CapabilitySyncMessage.SyncTarget.DIMENSION));
        }
    }

    @Override
    public ResourceLocation getCapKey() {
        return CAP_KEY;
    }

    @Override
    public int getEntityID() {
        return world.getDimension().getType().getId();
    }

    public void enqueue(Chunk chunk) {
        chunkQueue.add(chunk);
        if (!isActive) {
            thread = new Thread(this::doNext);
            thread.start();
            isActive = true;
        }
    }

    private void doNext() {
        Chunk chunk = chunkQueue.poll();

        ChunkPos pos = chunk.getPos();

        ChunkPos[] arr = {
                new ChunkPos(pos.x + 1, pos.z),
                new ChunkPos(pos.x - 1, pos.z),
                new ChunkPos(pos.x, pos.z + 1),
                new ChunkPos(pos.x, pos.z - 1),
                new ChunkPos(pos.x + 1, pos.z + 1),
                new ChunkPos(pos.x - 1, pos.z - 1),
                new ChunkPos(pos.x + 1, pos.z - 1),
                new ChunkPos(pos.x - 1, pos.z + 1),
        };

        if (world != null && !world.isRemote() && world.getDimension().isSurfaceWorld()) {
            PackHandler handler = PackHandler.forWorld(world);

            boolean flag = false;

            for (Biome b : chunk.getBiomes()) {
                if (ModEntities.wolfBiomes.contains(b)) {
                    flag = true;
                    break;
                }
            }

            if (!handler.closed.contains(pos) && flag) {
                if (!handler.isOccupied(pos)) {
                    if (handler.isRadiusClear(pos, 10)) {
                        if (world.getRandom().nextFloat() >= 0.99) {
                            handler.newPack(chunk);
                        }
                    } else {
                        Pack pack = null;

                        if (handler.isOccupied(arr[0])) {
                            pack = handler.getPackAt(arr[0]);
                        } else if (handler.isOccupied(arr[1])) {
                            pack = handler.getPackAt(arr[1]);
                        } else if (handler.isOccupied(arr[2])) {
                            pack = handler.getPackAt(arr[2]);
                        } else if (handler.isOccupied(arr[3])) {
                            pack = handler.getPackAt(arr[3]);
                        }

                        if (pack != null) {
                            handler.growPack(pack, chunk);
                        }
                    }
                }
                handler.closed.add(pos);
                handler.sync();
            }
        }

        if (chunkQueue.peek() != null) {
            doNext();
        } else {
            isActive = false;
            thread = null;
            Thread.currentThread().interrupt();
        }
    }

}
