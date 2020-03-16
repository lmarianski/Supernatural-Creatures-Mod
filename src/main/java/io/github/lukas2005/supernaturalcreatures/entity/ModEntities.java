package io.github.lukas2005.supernaturalcreatures.entity;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.items.ModItems;
import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//@ObjectHolder(Reference.MOD_ID)
public class ModEntities {

    private static int id = -1;

    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<EntityOmegaWerewolf>> WEREWOLF = register("werewolf",
            EntityType.Builder.create(EntityOmegaWerewolf::new, EntityClassification.MONSTER)
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

//    public static List<Integer> wolfBiomeIds;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> e) {
        addSpawn(WEREWOLF.get(), 6, 2, 5, EntityClassification.MONSTER, wolfBiomes);

        EntitySpawnPlacementRegistry.register(WEREWOLF.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, EntityOmegaWerewolf::spawnPlacement);
    }

//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void onBiomeRegister(RegistryEvent.Register<Biome> e) {
//        wolfBiomeIds = wolfBiomes.stream()
//                .map(Registry.BIOME::getId)
//                .collect(Collectors.toList());
//    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String key, EntityType.Builder<T> builder, int primaryColor, int secondaryColor) {
        EntityType<T> type = builder.build(key);
        ModItems.ITEMS.register(
                key,
                () -> new SpawnEggItem(type, primaryColor, secondaryColor,
                        new Item.Properties().group(ItemGroup.MISC)
                )
        );
        return ENTITIES.register(key, () -> type);
    }

    public static <T extends Entity> void addSpawn(EntityType<T> type, int weightedProb, int min, int max, EntityClassification classification, Biome... biomes) {
        for (Biome biome : biomes) {
            biome.getSpawns(classification).add(new Biome.SpawnListEntry(type, weightedProb, min, max));
        }
    }

    public static <T extends Entity> void addSpawn(EntityType<T> type, int weightedProb, int min, int max, EntityClassification classification, ArrayList<Biome> biomes) {
        for (Biome biome : biomes) {
            biome.getSpawns(classification).add(new Biome.SpawnListEntry(type, weightedProb, min, max));
        }
    }

    public static <T extends Entity> void addSpawnExcluding(EntityType<T> type, int weightedProb, int min, int max, EntityClassification classification, Biome... biomesToExclude) {
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
