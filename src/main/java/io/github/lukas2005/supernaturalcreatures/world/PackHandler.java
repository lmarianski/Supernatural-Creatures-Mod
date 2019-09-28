package io.github.lukas2005.supernaturalcreatures.world;

import io.github.lukas2005.supernaturalcreatures.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.network.CapabilitySyncMessage;
import io.github.lukas2005.supernaturalcreatures.network.ISyncable;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;

public class PackHandler implements IPackHandler, ISyncable {

    @CapabilityInject(IPackHandler.class)
    public static final Capability<IPackHandler> CAP = null;
    public static final ResourceLocation CAP_KEY = new ResourceLocation(Reference.MOD_ID, "pack_handler");

    public World world;

    public HashMap<ChunkPos, Pack> packChunks = new HashMap<>();
    public ArrayList<Pack> allPacks = new ArrayList<>();


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
        boolean flag = true;
        for (ChunkPos pos : poss) {
            if (isOccupied(pos)) {
                flag = false;
                break;
            }
        }
        return flag;
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
}
