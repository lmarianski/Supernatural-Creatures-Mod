package io.github.lukas2005.supernaturalcreatures.render;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class RenderHandler {

	public static void onLivingRender(RenderLivingEvent.Pre<?> e) {
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity();

			IPlayerDataCapability playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
			playerData.getCreatureType().getBehaviour().playerRenderOverride(playerData, player, e.getRenderer());
		}
	}

}
