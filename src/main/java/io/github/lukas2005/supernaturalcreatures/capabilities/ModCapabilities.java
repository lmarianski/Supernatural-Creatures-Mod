package io.github.lukas2005.supernaturalcreatures.capabilities;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class ModCapabilities {

	@CapabilityInject(IPlayerDataCapability.class)
	public static Capability<IPlayerDataCapability> PLAYER_DATA_CAPABILITY = null;

	public static final Map<String, Capability> CAPABILITY_MAP = new HashMap<>();

	public static void register() {
		IPlayerDataCapability.register();
		if (!ModCapabilities.CAPABILITY_MAP.containsValue(ModCapabilities.PLAYER_DATA_CAPABILITY)) ModCapabilities.CAPABILITY_MAP.put(IPlayerDataCapability.class.getName(), ModCapabilities.PLAYER_DATA_CAPABILITY);
	}

	@SubscribeEvent
	public static void onAttach(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof EntityPlayer) {
			e.addCapability(new ResourceLocation(Reference.MOD_ID, "PLAYER_DATA"), new IPlayerDataCapability.Provider());
		}
	}
}
