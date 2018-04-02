package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.behaviour.VampireBehaviour;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import io.github.lukas2005.supernaturalcreatures.proxy.IProxy;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {

	@Mod.Instance(Reference.MOD_ID)
	public static Main INSTANCE;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static IProxy proxy;

	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);

		logger = (Logger) e.getModLog();

		if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) logger.setLevel(Level.DEBUG);

		ModCapabilities.register();
		NetworkManager.init();
		for (int i = 0; i < VampireBehaviour.EYE_OVERLAY_COUNT; i++) {
			VampireBehaviour.eyeOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/vampire/eyes/eyes" + i + ".png"));
		}

		for (int i = 0; i < VampireBehaviour.FANG_OVERLAY_COUNT; i++) {
			VampireBehaviour.fangOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/vampire/fangs/fangs" + i + ".png"));
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
