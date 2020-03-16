package io.github.lukas2005.supernaturalcreatures.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public interface IPackHandler {

    World getWorld();

    ArrayList<Pack> getAllPacks();
    HashMap<ChunkPos, Pack> getPackChunks();

    Pack newPack(Chunk mainChunk);

    Pack getPackAt(ChunkPos pos);

    void growPack(Pack pack, Chunk chunk);

    boolean isOccupied(Chunk chunk);

    boolean isOccupied(ChunkPos pos);

    boolean isAllFree(ChunkPos[] poss);

    boolean isRadiusClear(ChunkPos center, int r);

    void sync();

    class Storage implements Capability.IStorage<IPackHandler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IPackHandler> capability, IPackHandler instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            ListNBT allPacks = new ListNBT();
            for (Pack pack : instance.getAllPacks()) {
                allPacks.add(pack.toNBT());
            }
            nbt.put("allPacks", allPacks);

            ListNBT packChunks = new ListNBT();
            for (Map.Entry<ChunkPos, Pack> entry : instance.getPackChunks().entrySet()) {
                CompoundNBT entryNBT = new CompoundNBT();
                entryNBT.putInt("chunkX", entry.getKey().x);
                entryNBT.putInt("chunkZ", entry.getKey().z);
                entryNBT.putUniqueId("packId", entry.getValue().id);
                packChunks.add(entryNBT);
            }
            nbt.put("packChunks", packChunks);

            ListNBT closedChunks = new ListNBT();
            for (ChunkPos pos : ((PackHandler)instance).closed) {
                CompoundNBT entryNBT = new CompoundNBT();
                entryNBT.putInt("chunkX", pos.x);
                entryNBT.putInt("chunkZ", pos.z);
                closedChunks.add(entryNBT);
            }
            nbt.put("closedChunks", closedChunks);

            return nbt;
        }

        @Override
        public void readNBT(Capability<IPackHandler> capability, IPackHandler instance, Direction side, INBT nbtBase) {
            CompoundNBT nbt = (CompoundNBT) nbtBase;

            for (INBT packNBT : (ListNBT) Objects.requireNonNull(nbt.get("allPacks"))) {
                instance.getAllPacks().add(Pack.fromNBT(packNBT, instance.getWorld()));
            }

            for (INBT inbt : (ListNBT) Objects.requireNonNull(nbt.get("packChunks"))) {
                CompoundNBT chunkNBT = (CompoundNBT) inbt;

                ChunkPos pos = new ChunkPos(chunkNBT.getInt("chunkX"), chunkNBT.getInt("chunkZ"));
                UUID id = chunkNBT.getUniqueId("packId");

                AtomicReference<Pack> pack = new AtomicReference<>();

                instance.getAllPacks().forEach(p -> {
                    if (p.id.equals(id)) {
                        pack.set(p);
                    }
                });

                if (pack.get() != null) {
                    instance.getPackChunks().put(pos, pack.get());
                }
            }

            for (INBT inbt : (ListNBT)Objects.requireNonNull(nbt.get("closedChunks"))) {
                CompoundNBT chunkNBT = (CompoundNBT) inbt;

                ChunkPos pos = new ChunkPos(chunkNBT.getInt("chunkX"), chunkNBT.getInt("chunkZ"));
                ((PackHandler)instance).closed.add(pos);
            }
        }

    }


    class Impl implements IPackHandler {

        @Override
        public World getWorld() {
            return null;
        }

        @Override
        public ArrayList<Pack> getAllPacks() {
            return null;
        }

        @Override
        public HashMap<ChunkPos, Pack> getPackChunks() {
            return null;
        }

        @Override
        public Pack newPack(Chunk mainChunk) {
            return null;
        }

        @Override
        public Pack getPackAt(ChunkPos pos) {
            return null;
        }

        @Override
        public void growPack(Pack pack, Chunk chunk) {

        }

        @Override
        public boolean isOccupied(Chunk chunk) {
            return false;
        }

        @Override
        public boolean isOccupied(ChunkPos pos) {
            return false;
        }

        @Override
        public boolean isAllFree(ChunkPos[] poss) {
            return false;
        }

        @Override
        public boolean isRadiusClear(ChunkPos center, int r) {
            return false;
        }

        @Override
        public void sync() {

        }
    }
}
