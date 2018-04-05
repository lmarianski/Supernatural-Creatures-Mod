package io.github.lukas2005.supernaturalcreatures.entity;

import io.github.lukas2005.supernaturalcreatures.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ModEntities {

	private static int id = -1;

	public static void registerEntities() {
		registerEntity(EntityVampire.class, "vampire", 80, 3, true);
		registerEggForEntity(EntityVampire.class, new Color(0,0,0), new Color(162,0,0));
		addSpawnAllBiomesExcluding(EntityVampire.class, 6, 1, 5, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.VOID);
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName, id++, Main.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	public static void registerEggForEntity(Class<? extends Entity> entityClass, Color primary, Color secondary) {
		EntityRegistry.registerEgg(entityClass, primary.getRGB(), secondary.getRGB());
	}

	public static void addSpawn(Class<? extends EntityLiving> entityClass, int weightedProb, int min, int max, EnumCreatureType type, Biome... biomes) {
		EntityRegistry.addSpawn(entityClass,  weightedProb, min, max, type, biomes);
	}

	public static void addSpawnAllBiomesExcluding(Class<? extends EntityLiving> entityClass, int weightedProb, int min, int max, EnumCreatureType type, ArrayList<Biome> biomesToExclude) {
		ArrayList<Biome> biomes = new ArrayList<>(ForgeRegistries.BIOMES.getValues());
		biomes.removeAll(biomesToExclude);
		EntityRegistry.addSpawn(entityClass,  weightedProb, min, max, type, biomes.toArray(new Biome[0]));
	}

	public static void addSpawnAllBiomesExcluding(Class<? extends EntityLiving> entityClass, int weightedProb, int min, int max, EnumCreatureType type, Biome... biomesToExclude) {
		addSpawnAllBiomesExcluding(entityClass, weightedProb, min, max, type, new ArrayList<>(Arrays.asList(biomesToExclude)));
	}
}
