package io.github.lukas2005.supernaturalcreatures.network;

import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.Utils;
import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerDataMessage implements IMessage {

	CreatureType type;

	public PlayerDataMessage() {}

	public PlayerDataMessage(IPlayerDataCapability cap) {
		type = cap.getCreatureType();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = CreatureType.byOrdinal(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type.ordinal());
	}

	public static class Handler implements IMessageHandler<PlayerDataMessage, IMessage> {

		@Override
		public IMessage onMessage(PlayerDataMessage message, MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(() -> {
				EntityPlayer player = Utils.getPlayerInstance(ctx);

				IPlayerDataCapability playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

				playerData.setCreatureType(message.type);
			});

			return null;
		}

	}
}
