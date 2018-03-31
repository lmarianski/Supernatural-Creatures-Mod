package io.github.lukas2005.supernaturalcreatures.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {

	}

	@Override
	public void init(FMLInitializationEvent e) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {

	}

	@Override
	public void registerBlockModel(Block block, int metadata, String variant) {
		registerItemModel(Item.getItemFromBlock(block), metadata, variant);
	}

	@Override
	public void registerItemModel(Item item, int metadata, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
	}
}
