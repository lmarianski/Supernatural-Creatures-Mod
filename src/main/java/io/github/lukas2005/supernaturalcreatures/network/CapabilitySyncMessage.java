package io.github.lukas2005.supernaturalcreatures.network;

import io.github.lukas2005.supernaturalcreatures.Utils;
import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CapabilitySyncMessage<T> implements IMessage {

	NBTTagCompound nbt;
	String instanceClassName;

	public CapabilitySyncMessage() {}

	public CapabilitySyncMessage(T instance, Capability<T> capability) {
		nbt = (NBTTagCompound) capability.writeNBT(instance, null);
		this.instanceClassName = instance.getClass().getDeclaringClass().getName();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbt = ByteBufUtils.readTag(buf);
		instanceClassName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
		ByteBufUtils.writeUTF8String(buf, instanceClassName);
	}

	public static class Handler implements IMessageHandler<CapabilitySyncMessage, IMessage> {

		@Override
		public IMessage onMessage(CapabilitySyncMessage message, MessageContext ctx) {

			Minecraft.getMinecraft().addScheduledTask(() -> {
				EntityPlayer player = Utils.getPlayerInstance(ctx);

				Capability cap = ModCapabilities.CAPABILITY_MAP.get(message.instanceClassName);

				cap.readNBT(player.getCapability(cap, null), null, message.nbt);
			});

			return null;
		}

	}
}
