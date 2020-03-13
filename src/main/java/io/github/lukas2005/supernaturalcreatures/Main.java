package io.github.lukas2005.supernaturalcreatures;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.WerewolfBehaviour;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.EnumPackRank;
import io.github.lukas2005.supernaturalcreatures.proxy.ClientProxy;
import io.github.lukas2005.supernaturalcreatures.proxy.IProxy;
import io.github.lukas2005.supernaturalcreatures.proxy.ServerProxy;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.EnumArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MOD_ID)
@Mod.EventBusSubscriber
public class Main {

    public static final String MOD_ID = "scm";
    public static final String NAME = "Supernatural Creatures Mod";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public Main() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);
        eventBus.addListener(this::setupClient);
        //eventBus.addListener(this::serverStarting);

    }

    private void setup(FMLCommonSetupEvent e) {
        //if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) logger.setLevel(Level.DEBUG);

        ModCapabilities.register();
        NetworkManager.init();
    }

    private void setupClient(FMLClientSetupEvent e) {
        proxy.setupClient(e);
    }

    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent e) {
        e.getCommandDispatcher().register(
                Commands.literal(Main.MOD_ID)
                        .then(Commands.literal("level").then(Commands.argument("level", IntegerArgumentType.integer(0)).executes((ctx) -> {
                            int level = ctx.getArgument("level", int.class);
                            if (ctx.getSource().assertIsEntity() instanceof PlayerEntity) {
                                SCMPlayer player = SCMPlayer.of(ctx.getSource().asPlayer());
                                player.setLevel(level);
                                player.syncData(true);
                            }
                            return 1;
                        })))
                        .then(Commands.literal("werewolf")
                                .then(Commands.literal("packRank").then(
                                        Commands.argument("rank", EnumArgument.enumArgument(EnumPackRank.class)).executes((ctx) -> {
                                            EnumPackRank rank = ctx.getArgument("rank", EnumPackRank.class);
                                            if (ctx.getSource().assertIsEntity() instanceof PlayerEntity) {
                                                SCMPlayer player = SCMPlayer.of(ctx.getSource().asPlayer());
                                                if (player.getCreatureType() == CreatureType.WEREWOLF) {
                                                    WerewolfBehaviour.WerewolfData data = (WerewolfBehaviour.WerewolfData) player.getCreatureData(CreatureType.WEREWOLF);
                                                    data.packRank = rank;
                                                    player.syncData(true);
                                                }
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("transformed").then(
                                        Commands.argument("true/false", BoolArgumentType.bool()).executes((ctx) -> {
                                            boolean transformed = ctx.getArgument("true/false", boolean.class);
                                            if (ctx.getSource().assertIsEntity() instanceof PlayerEntity) {
                                                SCMPlayer player = SCMPlayer.of(ctx.getSource().asPlayer());
                                                if (player.getCreatureType() == CreatureType.WEREWOLF) {
                                                    player.setTransformed(transformed);
                                                    player.syncData(true);
                                                }
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("skin").then(
                                        Commands.argument("skin", IntegerArgumentType.integer(0, WerewolfBehaviour.SKIN_OVERLAY_COUNT)).executes((ctx) -> {
                                            ResourceLocation skin = WerewolfBehaviour.skinOverlays.get(ctx.getArgument("skin", int.class));
                                            if (ctx.getSource().assertIsEntity() instanceof PlayerEntity) {
                                                SCMPlayer player = SCMPlayer.of(ctx.getSource().asPlayer());
                                                if (player.getCreatureType() == CreatureType.WEREWOLF) {
                                                    WerewolfBehaviour.WerewolfData data = (WerewolfBehaviour.WerewolfData) player.getCreatureData(CreatureType.WEREWOLF);
                                                    data.skin = skin;
                                                    player.syncData(true);
                                                }
                                            }
                                            return 1;
                                        })))
                        )
        );


    }


}
