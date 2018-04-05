package io.github.lukas2005.supernaturalcreatures.proxy;

import io.github.lukas2005.supernaturalcreatures.entity.EntityVampire;
import io.github.lukas2005.supernaturalcreatures.entity.render.EntityVampireRenderer;
import io.github.lukas2005.supernaturalcreatures.render.player.LayerPlayerSkinOverlay;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		//RenderManager manager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityVampire.class, manager -> new EntityVampireRenderer(manager, new ModelBiped(), 0.5F));
	}

	@Override
	public void init(FMLInitializationEvent e) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
			renderPlayer.addLayer(new LayerPlayerSkinOverlay(renderPlayer));
		}
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
