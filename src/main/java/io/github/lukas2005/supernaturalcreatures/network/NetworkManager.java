package io.github.lukas2005.supernaturalcreatures.network;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkManager {

	public static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Reference.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = -1;

	public static void init() {
		//INSTANCE.registerMessage(DamageEntityMessage.Handler.class, DamageEntityMessage.class, id++, Side.SERVER);

		INSTANCE.registerMessage(id++, CapabilitySyncMessage.class, CapabilitySyncMessage::encode, CapabilitySyncMessage::decode, CapabilitySyncMessage::handle);

		//NetworkRegistry.INSTANCE.registerGuiHandler(Main.INSTANCE, new GuiHandler());
	}

}
