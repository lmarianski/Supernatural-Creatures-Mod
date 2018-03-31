package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class EventHandler {

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity();
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
		if (e.phase == TickEvent.Phase.END) {
			IPlayerDataCapability playerData = e.player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, EnumFacing.DOWN);
			if (playerData != null) Main.logger.info("Side: " + e.side + " State: " + playerData.getCreatureType());
		}
	}

}
