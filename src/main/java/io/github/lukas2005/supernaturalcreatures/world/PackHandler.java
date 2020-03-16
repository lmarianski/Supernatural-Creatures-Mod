package io.github.lukas2005.supernaturalcreatures.world;

import io.github.lukas2005.supernaturalcreatures.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.Reference;
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
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber
public class PackHandler implements IPackHandler, ISyncable {

    @CapabilityInject(IPackHandler.class)
    public static final Capability<IPackHandler> CAP = null;
    public static final ResourceLocation CAP_KEY = new ResourceLocation(Reference.MOD_ID, "pack_handler");

    public static final Logger LOGGER = LogManager.getLogger();

    public World world;
    private final Random random;

    public HashMap<ChunkPos, Pack> packChunks = new HashMap<>();
    public ArrayList<Pack> allPacks = new ArrayList<>();

    public Set<ChunkPos> closed = new HashSet<>();

    private volatile LinkedList<Chunk> chunkQueue = new LinkedList<>();

    private volatile Thread thread;

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

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e) {
        if (e.getWorld() != null && !e.getWorld().isRemote()) {
            PackHandler handler = PackHandler.forWorld((World) e.getWorld());

            if (!handler.closed.contains(e.getChunk().getPos())) {
                handler.enqueue((Chunk) e.getChunk());
            }
        }
    }

    public PackHandler(World world) {
        this.world = world;
        this.random = new Random(world.getSeed());

        this.thread = new Thread(() -> {
           while (!Thread.interrupted()) {
               if (chunkQueue.peek() != null) {
                   doNext();
                   sync();
               } else {
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       Thread.currentThread().interrupt();
                   }
               }
           }
        });
        this.thread.start();
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
//        if (thread == null || !thread.isAlive()) {
//            thread = new Thread(this::doNext, "Pack Handler Thread");
//            thread.start();
//        }
    }

    private void doNext() {
        long start = System.currentTimeMillis();
        //world.getProfiler().startSection("werewolf_packHandler");
        Chunk chunk = chunkQueue.poll();

        if (world != null && chunk != null) {
            ChunkPos pos = chunk.getPos();

            if (!world.isRemote() && world.getDimension().isSurfaceWorld()) {
                boolean flag = false;

                for (Biome b : chunk.getBiomes().biomes) {
                    if (ModEntities.wolfBiomes.contains(b)) {
                        flag = true;
                        break;
                    }
                }

                if (flag && !closed.contains(pos)) {
                    if (!isOccupied(pos)) {
                        if (isRadiusClear(pos, 10)) {
                            if (random.nextFloat() >= 0.99) {
                                newPack(chunk);
                            }
                        } else {
                            Pack pack = null;

                            ChunkPos[] arr = {
                                    new ChunkPos(pos.x + 1, pos.z),
                                    new ChunkPos(pos.x - 1, pos.z),
                                    new ChunkPos(pos.x, pos.z + 1),
                                    new ChunkPos(pos.x, pos.z - 1),
//                new ChunkPos(pos.x + 1, pos.z + 1),
//                new ChunkPos(pos.x - 1, pos.z - 1),
//                new ChunkPos(pos.x + 1, pos.z - 1),
//                new ChunkPos(pos.x - 1, pos.z + 1),
                            };

                            if (isOccupied(arr[0])) {
                                pack = getPackAt(arr[0]);
                            } else if (isOccupied(arr[1])) {
                                pack = getPackAt(arr[1]);
                            } else if (isOccupied(arr[2])) {
                                pack = getPackAt(arr[2]);
                            } else if (isOccupied(arr[3])) {
                                pack = getPackAt(arr[3]);
                            }

                            if (pack != null) {
                                LOGGER.debug("New pack territory at " + pos.asBlockPos() + " Owned by " + pack.id);
                                growPack(pack, chunk);
                            }
                        }
                    }
                    closed.add(pos);
                }
            }

            if (chunkQueue.peek() != null) {
                doNext();
            }
        }
        //world.getProfiler().endSection();
        //LOGGER.debug("Pack Handler took "+(System.currentTimeMillis()-start)+"ms");
    }

}
