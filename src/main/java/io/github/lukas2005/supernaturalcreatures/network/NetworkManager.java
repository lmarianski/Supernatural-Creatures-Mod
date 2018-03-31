package io.github.lukas2005.supernaturalcreatures.network;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	static int id = -1;

	public static void init() {
		INSTANCE.registerMessage(PlayerDataMessage.Handler.class, PlayerDataMessage.class, id++, Side.CLIENT);
	}

}
