package io.github.lukas2005.supernaturalcreatures.behaviour;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.render.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class WerewolfBehaviour extends DefaultCreatureBehaviour {

	@Override
	public void playerRenderOverride(IPlayerDataCapability playerData, EntityPlayer player, RenderLivingBase<?> renderer) {
		//if (playerData.isTransformed()) Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation());
	}

	@Override
	public void playerSkinOverlayRender(IPlayerDataCapability playerData, AbstractClientPlayer player, RenderPlayer playerRenderer, float scale, float partialTicks) {
		//int eyeType = Math.max(-1, Math.min(Integer.parseInt(playerData.getData("vampire.eyes")), eyeOverlays.size() - 1));

		ResourceLocation eyes = null;

//		if (eyeType > -1) {
//			eyes = eyeOverlays.get(eyeType);
//		}

		if (player.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		if (eyes != null) {
			RenderUtil.renderGlowing(playerRenderer, playerRenderer.getMainModel().bipedHead, eyes, 240f, player, scale, partialTicks);
		}
	}
}
