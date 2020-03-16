package io.github.lukas2005.supernaturalcreatures.network;

import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CapabilitySyncMessage implements IMessage {

	public CompoundNBT nbt;
	public String capKey;
	public int entityId;
	public SyncTarget target;

	public CapabilitySyncMessage() {}

	public CapabilitySyncMessage(ISyncable instance, Capability capability, SyncTarget target) {
		nbt = (CompoundNBT) capability.writeNBT(instance, null);
		this.capKey = instance.getCapKey().toString();
		this.entityId = instance.getEntityID();
		this.target = target;
	}

	public static void encode(CapabilitySyncMessage message, PacketBuffer buf) {
		buf.writeCompoundTag(message.nbt);
		buf.writeString(message.capKey);
		buf.writeInt(message.entityId);
		buf.writeEnumValue(message.target);
	}

	public static CapabilitySyncMessage decode(PacketBuffer buf) {
		CapabilitySyncMessage message = new CapabilitySyncMessage();
		message.nbt = buf.readCompoundTag();
		message.capKey = buf.readString();
		message.entityId = buf.readInt();
		message.target = buf.readEnumValue(SyncTarget.class);

		return message;
	}

	public static void handle(CapabilitySyncMessage message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			World world = Main.proxy.getNetworkWorld(ctx.get());
			Capability cap = ModCapabilities.CAPABILITY_MAP.get(new ResourceLocation(message.capKey));

			try {
				ISyncable s = null;
				switch (message.target) {
					case ENTITY:
						Entity e = world.getEntityByID(message.entityId);
						s = (ISyncable) e.getCapability(cap).orElseThrow(()->new Exception("Null capability!"));
						break;
					case DIMENSION:
						if (world.getDimension().getType().getId() == message.entityId) {
							s = (ISyncable) world.getCapability(cap).orElseThrow(()->new Exception("Null capability!"));
						}
						break;
				}

				if (s != null)
					cap.readNBT(s, null, message.nbt);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}

		});
		ctx.get().setPacketHandled(true);
	}

	public enum SyncTarget {
		ENTITY,
		PLAYER,
		DIMENSION
	}

}
