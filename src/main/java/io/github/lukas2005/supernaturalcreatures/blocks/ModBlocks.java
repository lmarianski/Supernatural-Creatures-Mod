package io.github.lukas2005.supernaturalcreatures.blocks;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Reference.MOD_ID);

	public static final RegistryObject<Block> SILVER_ORE = register(
			"silver_ore",
			new OreBlock(Properties.create(Material.ROCK)
			.hardnessAndResistance(3.0F, 3.0F)),
			new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)
	);

	public static final RegistryObject<Block> SILVER_BLOCK = register(
			"silver_block",
			new OreBlock(Properties.create(Material.IRON)
					.hardnessAndResistance(3.0F, 6.0F)),
			new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)
	);

	@SubscribeEvent
	public static void onRegisterBlockModels(ModelRegistryEvent e) {
//		for (Block block : blocks) {
//			if (block instanceof IHasModel) {
//				((IHasModel) block).registerModels();
//			} else {
//				//Main.proxy.registerBlockModel(block, 0, "inventory");
//			}
//		}
	}

	public static RegistryObject<Block> register(String name, Block block, Item.Properties properties) {
		return register(name, block, new BlockItem(block, properties));
	}

	public static RegistryObject<Block> register(String name, Block block) {
		return register(name, block, new Item.Properties());
	}

	public static RegistryObject<Block> register(String name, Block block, BlockItem itemBlock) {

		ModItems.ITEMS.register(name, () -> itemBlock);
		return BLOCKS.register(name, () -> block);
	}
}
