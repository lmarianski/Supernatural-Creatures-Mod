package io.github.lukas2005.supernaturalcreatures.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {

	void preInit(FMLPreInitializationEvent e);

	void init(FMLInitializationEvent e);

	void postInit(FMLPostInitializationEvent e);

	default void registerBlockModel(Block block, int metadata, String variant) {}
	default void registerItemModel(Item item, int metadata, String variant) {}

}
