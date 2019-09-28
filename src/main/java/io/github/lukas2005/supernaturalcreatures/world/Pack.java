package io.github.lukas2005.supernaturalcreatures.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.UUID;

public class Pack {

    public Chunk mainTerritoryChunk;
    public ArrayList<Chunk> territoryList = new ArrayList<>();
    public UUID id;
    public int maxSize;

    private Pack() {}

    public Pack(Chunk mainChunk) {
        this.mainTerritoryChunk = mainChunk;
        this.territoryList.add(mainChunk);
        this.maxSize = mainChunk.getWorld().getRandom().nextInt(100)+50;

        this.id = UUID.randomUUID();
    }

    public INBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putUniqueId("uuid", this.id);
        nbt.putInt("mainChunkX", this.mainTerritoryChunk.getPos().x);
        nbt.putInt("mainChunkZ", this.mainTerritoryChunk.getPos().z);

        ListNBT list = new ListNBT();

        for (Chunk chunk : territoryList) {
            CompoundNBT cNBT = new CompoundNBT();

            cNBT.putInt("chunkX", chunk.getPos().x);
            cNBT.putInt("chunkZ", chunk.getPos().z);

            list.add(cNBT);
        }

        nbt.put("territoryList", list);

        nbt.putInt("maxSize", this.maxSize);

        return nbt;
    }

    public static Pack fromNBT(INBT inbt, World world) {
        CompoundNBT nbt = (CompoundNBT) inbt;
        Pack pack = new Pack();

        pack.id = nbt.getUniqueId("uuid");
        pack.mainTerritoryChunk = world.getChunk(nbt.getInt("mainChunkX"), nbt.getInt("mainChunkZ"));

        for (INBT iNBT : nbt.getList("territoryList", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT cNBT = (CompoundNBT) iNBT;

            pack.territoryList.add(world.getChunk(cNBT.getInt("chunkX"), cNBT.getInt("chunkZ")));
        }

        pack.maxSize = nbt.getInt("maxSize");

        return pack;
    }
}
