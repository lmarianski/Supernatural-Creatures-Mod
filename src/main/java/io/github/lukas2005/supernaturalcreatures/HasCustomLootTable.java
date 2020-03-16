package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.data.loot.BlockLootTables;

public interface HasCustomLootTable {

    void registerLootTable(BlockLootTables generator);

}
