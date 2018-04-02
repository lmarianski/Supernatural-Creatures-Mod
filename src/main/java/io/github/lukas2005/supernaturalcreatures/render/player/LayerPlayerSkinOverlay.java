package io.github.lukas2005.supernaturalcreatures.render.player;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Not yet sure if i really wanna this
 */
@SideOnly(Side.CLIENT)
public class LayerPlayerSkinOverlay implements LayerRenderer<AbstractClientPlayer> {
	private final RenderPlayer playerRenderer;

	private IPlayerDataCapability playerData = null;

	public LayerPlayerSkinOverlay(RenderPlayer playerRendererIn) {
		this.playerRenderer = playerRendererIn;
	}

	public void doRenderLayer(AbstractClientPlayer player, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
		if (playerData == null) {
			playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
		}

		GlStateManager.pushMatrix();
		playerData.getCreatureType().getBehaviour().playerSkinOverlayRender(playerData, player, playerRenderer, scale, partialTicks);
		GlStateManager.popMatrix();
	}

	public boolean shouldCombineTextures() {
		return true;
	}
}