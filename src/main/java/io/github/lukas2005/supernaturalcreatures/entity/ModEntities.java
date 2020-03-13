package io.github.lukas2005.supernaturalcreatures.entity;

import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//@ObjectHolder(Reference.MOD_ID)
public class ModEntities {

    private static int id = -1;

    private static ArrayList<EntityType> entities = new ArrayList<>();
    private static ArrayList<Item> spawnEggs = new ArrayList<>();

    public static final EntityType<EntityVampire> VAMPIRE = register("vampire", EntityType.Builder.create(EntityVampire::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3),
            new Color(255, 0, 0).getRGB(), new Color(0, 0, 0).getRGB()
    );

    public static final EntityType<EntityWerewolf> WEREWOLF = register("werewolf", EntityType.Builder.create(EntityWerewolf::new, EntityClassification.MONSTER)
                    .size(0.6F, 1.95F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3),
            new Color(107, 124, 129).getRGB(), new Color(182, 202, 207).getRGB()
    );

    public static final ArrayList<Biome> wolfBiomes = new ArrayList<>(Arrays.asList(
            Biomes.FOREST,
            Biomes.BIRCH_FOREST,
            Biomes.BIRCH_FOREST_HILLS,
            Biomes.DARK_FOREST,
            Biomes.DARK_FOREST_HILLS,
            Biomes.TALL_BIRCH_FOREST,
            Biomes.TAIGA,
            Biomes.TAIGA_HILLS,
            Biomes.TAIGA_MOUNTAINS,
            Biomes.SNOWY_TAIGA,
            Biomes.SNOWY_TAIGA_HILLS,
            Biomes.SNOWY_TAIGA_MOUNTAINS,
            Biomes.GIANT_TREE_TAIGA,
            Biomes.GIANT_TREE_TAIGA_HILLS,
            Biomes.GIANT_SPRUCE_TAIGA,
            Biomes.GIANT_SPRUCE_TAIGA_HILLS));

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> e) {
        e.getRegistry().registerAll(entities.toArray(new EntityType[]{}));
        entities.clear();

        addSpawnExcluding(VAMPIRE, 6, 1, 2, EntityClassification.MONSTER, Biomes.NETHER, Biomes.THE_END, Biomes.THE_VOID);
        //addSpawnExcluding(WEREWOLF, 6, 1, 5, EntityClassification.MONSTER, Biomes.NETHER, Biomes.THE_END, Biomes.THE_VOID);

        addSpawn(WEREWOLF, 6, 2, 5, EntityClassification.MONSTER, wolfBiomes);


        EntitySpawnPlacementRegistry.register(VAMPIRE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityVampire::spawnPlacement);
        EntitySpawnPlacementRegistry.register(WEREWOLF, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, EntityWerewolf::spawnPlacement);
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> e) {
        e.getRegistry().registerAll(spawnEggs.toArray(new Item[]{}));
        spawnEggs.clear();
    }

    public static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder, int primaryColor, int secondaryColor) {
        EntityType<T> type = builder.build(key);
        type.setRegistryName(new ResourceLocation(Reference.MOD_ID, key));

        entities.add(type);
        spawnEggs.add(new SpawnEggItem(type, primaryColor, secondaryColor, new Item.Properties()).setRegistryName(Reference.MOD_ID, key));

        return type;
    }

    public static void addSpawn(EntityType type, int weightedProb, int min, int max, EntityClassification classification, Biome... biomes) {
        for (Biome biome : biomes) {
            biome.getSpawns(classification).add(new Biome.SpawnListEntry(type, weightedProb, min, max));
        }
    }

    public static void addSpawn(EntityType type, int weightedProb, int min, int max, EntityClassification classification, ArrayList<Biome> biomes) {
        for (Biome biome : biomes) {
            biome.getSpawns(classification).add(new Biome.SpawnListEntry(type, weightedProb, min, max));
        }
    }

    public static void addSpawnExcluding(EntityType type, int weightedProb, int min, int max, EntityClassification classification, Biome... biomesToExclude) {
        ArrayList<Biome> biomes = new ArrayList<>(ForgeRegistries.BIOMES.getValues());
        biomes.removeAll(Arrays.asList(biomesToExclude));
        biomes.forEach(biome -> {
            biome.getSpawns(classification).add(new Biome.SpawnListEntry(type, weightedProb, min, max));
        });
    }


    public static boolean spawnPredicateLight(IWorld world, BlockPos blockPos, Random random) {
        if (world.getLightFor(LightType.SKY, blockPos) > random.nextInt(32)) {
            return false;
        } else {
            int lvt_3_1_ = world.getWorld().isThundering() ? world.getNeighborAwareLightSubtracted(blockPos, 10) : world.getLight(blockPos);
            return lvt_3_1_ <= random.nextInt(8);
        }
    }

    public static boolean spawnPredicateCanSpawn(EntityType<? extends MobEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        BlockPos blockpos = blockPos.down();
        return spawnReason == SpawnReason.SPAWNER || world.getBlockState(blockpos).canEntitySpawn(world, blockpos, entityType);
    }
}
