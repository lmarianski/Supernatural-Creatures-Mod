package io.github.lukas2005.supernaturalcreatures.blocks;

import io.github.lukas2005.supernaturalcreatures.IHasModel;
import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
	private static ArrayList<Block> blocks = new ArrayList<>();
	private static ArrayList<Item> itemBlocks = new ArrayList<>();

	public static void initBlocks() {

	}

	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> e) {
		initBlocks();
		e.getRegistry().registerAll(blocks.toArray(new Block[]{}));
	}

	@SubscribeEvent
	public static void onRegisterItemBlocks(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(itemBlocks.toArray(new Item[]{}));
	}

	@SubscribeEvent
	public static void onRegisterBlockModels(ModelRegistryEvent e) {
		for (Block block : blocks) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
			} else {
				//Main.proxy.registerBlockModel(block, 0, "inventory");
			}
		}
	}

	public static Block initHelper(Block block, String name) {
//		block.setUnlocalizedName(Reference.MOD_ID+"."+name)
				block.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));

		blocks.add(block);

		itemBlocks.add(new BlockItem(block, new Item.Properties())
				//.setUnlocalizedName(Reference.MOD_ID+"."+name)
				.setRegistryName(new ResourceLocation(Reference.MOD_ID, name)));

		return block;
	}

	public static Block initHelper(Block block, BlockItem itemBlock, String name) {

		blocks.add(block
				//.setUnlocalizedName(Reference.MOD_ID+"."+name)
				.setRegistryName(new ResourceLocation(Reference.MOD_ID, name))
		);

		itemBlocks.add(itemBlock
				//.setUnlocalizedName(Reference.MOD_ID+"."+name)
				.setRegistryName(new ResourceLocation(Reference.MOD_ID, name))
		);

		return block;
	}
}
