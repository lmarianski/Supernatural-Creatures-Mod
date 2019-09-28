package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
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

	/**
	 * Checks if the entity can get sundamage at it's current position.
	 * It is recommend to cache the value for a few ticks.
	 *
	 * @param entity
	 * @return
	 */
	public static boolean gettingSundamge(LivingEntity entity) {
		entity.getEntityWorld().getProfiler().startSection(Reference.MOD_ID+"_checkSundamage");
		if (entity instanceof PlayerEntity && entity.isSpectator()) return false;
		//if (API.sundamageRegistry().getSundamageInDim(entity.getEntityWorld().getWorldInfo().getD)) {
			if (!entity.getEntityWorld().isRaining()) {
				float angle = entity.getEntityWorld().getCelestialAngle(1.0F);
				//TODO maybe use this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)
				if (angle > 0.78 || angle < 0.24) {
					BlockPos pos = new BlockPos(entity.posX + 0.5, entity.posY + 0, entity.posZ + 0.5);

					if (canBlockSeeSun(entity.getEntityWorld(), pos)) {
						try {
							Biome biome = entity.getEntityWorld().getBiome(pos);
							//if (API.sundamageRegistry().getSundamageInBiome(biome)) {
								entity.getEntityWorld().getProfiler().endSection();
								return true;
						//	}
						} catch (NullPointerException e) {
							//Strange thing which happen in 1.7.10, not sure about 1.8
						}

					}
				}

			}
		//}
		entity.getEntityWorld().getProfiler().endSection();

		return false;
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

	public static List<Skill> getHighestLevelSkills(List<Skill> skills) {
		List<Skill> ret = new ArrayList<>();

		for (Skill s : skills) {
			if (s.getDependants().size() > 0) {
				ret.addAll(getHighestLevelSkills(s.getDependants()));
			} else  {
				if (skills.contains(s)) ret.add(s);
			}
		}
		return new ArrayList<>(new HashSet<>(ret));
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
