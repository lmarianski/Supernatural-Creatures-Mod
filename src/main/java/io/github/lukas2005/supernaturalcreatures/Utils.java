package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.lang.reflect.Field;


public class Utils {

	public static EntityPlayer getPlayerInstance(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : ctx.getServerHandler().playerEntity);
	}

	public static Field getField(Class<?> clazz, String name) throws Exception {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			try {
				return getField(clazz.getSuperclass(), name);
			} catch (Exception e1) {
				throw e1;
			}
		}
	}

}
