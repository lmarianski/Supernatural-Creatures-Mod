package io.github.lukas2005.supernaturalcreatures;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.lukas2005.supernaturalcreatures.blocks.ModBlocks;
import io.github.lukas2005.supernaturalcreatures.data.LootTablesGenerator;
import io.github.lukas2005.supernaturalcreatures.data.RecipesGenerator;
import io.github.lukas2005.supernaturalcreatures.entity.ModEntities;
import io.github.lukas2005.supernaturalcreatures.enums.EnumForm;
import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;
import io.github.lukas2005.supernaturalcreatures.items.ModItems;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.skills.WerewolfSkills;
import io.github.lukas2005.supernaturalcreatures.potions.ModEffects;
import io.github.lukas2005.supernaturalcreatures.proxy.ClientProxy;
import io.github.lukas2005.supernaturalcreatures.proxy.IProxy;
import io.github.lukas2005.supernaturalcreatures.proxy.ServerProxy;
import io.github.lukas2005.supernaturalcreatures.render.entity.EntityWerewolfRenderer;
import io.github.lukas2005.supernaturalcreatures.render.player.LayerSkinOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.command.Commands;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.EnumArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
@Mod.EventBusSubscriber
public class Main {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public Main() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);
        eventBus.addListener(this::gatherData);

        ModBlocks.BLOCKS.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModEntities.ENTITIES.register(eventBus);
        ModEffects.EFFECTS.register(eventBus);
        WerewolfSkills.SKILLS.register(eventBus);
    }

    private void gatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();

        if (e.includeServer()) {
            generator.addProvider(new LootTablesGenerator(generator));
            generator.addProvider(new RecipesGenerator(generator));
        }

    }

    private void setup(FMLCommonSetupEvent e) {
        ModCapabilities.register();
        NetworkManager.init();

        ModFactions.init();

        proxy.setup(e);
    }

    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent e) {
        e.getServer().getResourceManager().addReloadListener(new ModTags());

        e.getCommandDispatcher().register(
                Commands.literal(Reference.MOD_ID)
                        .then(Commands.literal("werewolf")
                                .then(Commands.literal("packRank").then(
                                        Commands.argument("rank", EnumArgument.enumArgument(EnumPackRank.class)).executes((ctx) -> {
                                            EnumPackRank rank = ctx.getArgument("rank", EnumPackRank.class);
                                            if (ctx.getSource().assertIsEntity() instanceof PlayerEntity) {
                                                WerewolfPlayer player = WerewolfPlayer.of(ctx.getSource().asPlayer());
                                                player.setPackRank(rank);
                                                player.sync(true);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("transformed").then(
                                        Commands.argument("true/false", BoolArgumentType.bool()).executes((ctx) -> {
                                            boolean transformed = ctx.getArgument("true/false", boolean.class);
                                            if (ctx.getSource().assertIsEntity() instanceof PlayerEntity) {
                                                WerewolfPlayer player = WerewolfPlayer.of(ctx.getSource().asPlayer());
                                                player.setForm(transformed ? EnumForm.HYBRID : EnumForm.HUMAN);
                                                player.sync(true);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("skin").then(
                                        Commands.argument("skin", IntegerArgumentType.integer(0, WerewolfPlayer.SKIN_OVERLAY_COUNT)).executes((ctx) -> {
                                            ResourceLocation skin = WerewolfPlayer.skinOverlays.get(ctx.getArgument("skin", int.class));
                                            if (ctx.getSource().assertIsEntity() instanceof PlayerEntity) {
                                                WerewolfPlayer player = WerewolfPlayer.of(ctx.getSource().asPlayer());
                                                player.setSkin(skin);
                                                player.sync(true);
                                            }
                                            return 1;
                                        })))
                        )
        );


    }


}
