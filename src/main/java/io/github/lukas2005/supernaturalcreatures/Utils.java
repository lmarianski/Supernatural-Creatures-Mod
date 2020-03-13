package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;


public class Utils {

//	public static PlayerEntity getPlayerInstance(MessageContext ctx) {
//		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : ctx.getServerHandler().playerEntity);
//	}

	public static Field getField(Class<?> clazz, String name) throws Exception {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			return getField(clazz.getSuperclass(), name);
		}
	}

	public static boolean canBlockSeeSun(World world, BlockPos pos) {
		if (pos.getY() >= world.getSeaLevel()) {
			return world.canBlockSeeSky(pos);
		} else {
			BlockPos blockpos = new BlockPos(pos.getX(), world.getSeaLevel(), pos.getZ());

			if (!world.canBlockSeeSky(blockpos)) {
				return false;
			} else {
				for (blockpos = blockpos.down(); blockpos.getY() > pos.getY(); blockpos = blockpos.down()) {
					BlockState iblockstate = world.getBlockState(blockpos);

					if (iblockstate.getBlock().getOpacity(iblockstate, world, blockpos) > 0) {
						return false;
					}
				}

				return true;
			}
		}
	}

	public static <T> boolean containsAny(List<T> list1, List<T> list2) {

		for (T o : list2) {
			if (list1.contains(o)) {
				return true;
			}
		}
		return false;
	}

//	public static IThreadListener getThreadListener(MessageContext ctx) {
//		return ctx.side == Side.CLIENT ? Minecraft.getMinecraft() : ctx.getServerHandler().playerEntity.mcServer;
//	}

	public static void setFinal(Field field, Object obj, Object newValue) throws Exception {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(obj, newValue);
	}

	public static long timeUntilNextFullMoon(long dayTime) {
		return (192000 - (dayTime % 192000)) + 18000;
	}
}
