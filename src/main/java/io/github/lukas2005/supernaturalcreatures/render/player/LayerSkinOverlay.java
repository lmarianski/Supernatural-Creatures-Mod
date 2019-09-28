package io.github.lukas2005.supernaturalcreatures.render.player;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class LayerSkinOverlay<T extends AbstractClientPlayerEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {
	private final LivingRenderer<T, M> playerRenderer;

	public LayerSkinOverlay(LivingRenderer<T, M> playerRendererIn) {
		super(playerRendererIn);
		this.playerRenderer = playerRendererIn;
	}

	@Override
	public void render(T player, float v, float v1, float partialTicks, float v3, float v4, float v5, float scale) {
		SCMPlayer playerData = SCMPlayer.of(player);

		GlStateManager.pushMatrix();
		playerData.getCreatureType().getBehaviour().playerSkinOverlayRender(playerData, player, playerRenderer, scale, partialTicks);
		GlStateManager.popMatrix();
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}