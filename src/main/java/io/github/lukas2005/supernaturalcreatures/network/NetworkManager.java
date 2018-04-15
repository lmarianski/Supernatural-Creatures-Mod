package io.github.lukas2005.supernaturalcreatures.network;

import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.gui.GuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	private static int id = -1;

	public static void init() {
		INSTANCE.registerMessage(CapabilitySyncMessage.Handler.class, CapabilitySyncMessage.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(CapabilitySyncMessage.Handler.class, CapabilitySyncMessage.class, id++, Side.SERVER);

		NetworkRegistry.INSTANCE.registerGuiHandler(Main.INSTANCE, new GuiHandler());
	}

}
