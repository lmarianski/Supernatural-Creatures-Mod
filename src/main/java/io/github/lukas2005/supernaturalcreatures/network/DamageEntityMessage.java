//package io.github.lukas2005.supernaturalcreatures.network;
//
//import io.github.lukas2005.supernaturalcreatures.DamageSources;
//import io.github.lukas2005.supernaturalcreatures.Utils;
//import io.netty.buffer.ByteBuf;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.EntityDamageSource;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.network.ByteBufUtils;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//
//public class DamageEntityMessage {
//
//	int victim;
//	int attacker;
//	String source;
//	float damage;
//
//	public DamageEntityMessage() {}
//
//	public DamageEntityMessage(Entity attacker, Entity victim, DamageSource source, float damage) {
//		this.source = source.getDamageType();
//		this.damage = damage;
//		this.victim = victim.getEntityId();
//		this.attacker = attacker.getEntityId();
//	}
//
//	@Override
//	public void fromBytes(ByteBuf buf) {
//		damage = buf.readFloat();
//		source = ByteBufUtils.readUTF8String(buf);
//		victim = buf.readInt();
//		attacker = buf.readInt();
//	}
//
//	@Override
//	public void toBytes(ByteBuf buf) {
//		buf.writeFloat(damage);
//		ByteBufUtils.writeUTF8String(buf, source);
//		buf.writeInt(victim);
//		buf.writeInt(attacker);
//	}
//
//	public static class Handler implements IMessageHandler<DamageEntityMessage, IMessage> {
//
//		@Override
//		public IMessage onMessage(DamageEntityMessage message, MessageContext ctx) {
//
//			Utils.getThreadListener(ctx).addScheduledTask(() -> {
//				World world = ctx.getServerHandler().playerEntity.worldObj;
//				world.getEntityByID(message.victim).attackEntityFrom(new EntityDamageSource(message.source, world.getEntityByID(message.attacker)), message.damage);
//			});
//
//			return null;
//		}
//	}
//
//}
