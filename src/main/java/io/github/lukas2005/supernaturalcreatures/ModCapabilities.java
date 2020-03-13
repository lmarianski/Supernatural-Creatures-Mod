package io.github.lukas2005.supernaturalcreatures;

import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.world.PackHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class ModCapabilities {

	public static final Map<ResourceLocation, Capability> CAPABILITY_MAP = new HashMap<>();

	public static void register() {
		PackHandler.registerCapability();
	}

	@SubscribeEvent
	public static void onAttachEntity(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof PlayerEntity) {
		}
	}

	@SubscribeEvent
	public static void onAttachWorld(AttachCapabilitiesEvent<World> e) {
		if (e.getObject().getDimension().isSurfaceWorld()) {
			e.addCapability(PackHandler.CAP_KEY, PackHandler.createNewCapability(e.getObject()));
		}
	}

}
