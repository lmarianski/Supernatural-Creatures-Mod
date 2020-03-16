package io.github.lukas2005.supernaturalcreatures.blocks;

import io.github.lukas2005.supernaturalcreatures.HasCustomLootTable;
import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Reference.MOD_ID);

	public static final RegistryObject<Block> SILVER_ORE = register(
			"silver_ore",
			() -> new OreBlock(Properties.create(Material.ROCK)
				.hardnessAndResistance(3.0F, 3.0F)),
			new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)
	);

	public static final RegistryObject<Block> SILVER_BLOCK = register(
			"silver_block",
			() -> new Block(Properties.create(Material.IRON)
					.hardnessAndResistance(3.0F, 6.0F)),
			new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)
	);

	public static RegistryObject<Block> register(String name, Supplier<Block> block, Item.Properties properties) {
		RegistryObject<Block> b = BLOCKS.register(name, block);
		ModItems.ITEMS.register(name, () -> new BlockItem(b.get(), properties));
		return b;
	}

	public static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, new Item.Properties());
	}

	public static class LootTable extends BlockLootTables {

		@Override
		protected void addTables() {
			BLOCKS.getEntries().stream()
					.map(RegistryObject::get)
					.forEach(block -> {
						if (block instanceof HasCustomLootTable) {
							((HasCustomLootTable) block).registerLootTable(this);
						} else {
							this.registerDropSelfLootTable(block);
						}
					});
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return BLOCKS.getEntries().stream()
					.map(RegistryObject::get)
					.collect(Collectors.toList());
		}

	}
}
