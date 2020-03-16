package io.github.lukas2005.supernaturalcreatures;

import de.teamlapen.lib.HelperRegistry;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import de.teamlapen.vampirism.player.VampirismPlayer;
import io.github.lukas2005.supernaturalcreatures.api.entity.werewolf.IWerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ModFactions {

    public static IPlayableFaction<IWerewolfPlayer> WEREWOLF_FACTION = null;

    public static void init() {
        WerewolfPlayer.registerCapability();

        WEREWOLF_FACTION = register("werewolf",
                new Color(192, 192, 192),
                30,
                IWerewolfPlayer.class,
                WerewolfPlayer.class,
                WerewolfPlayer.CAP,
                WerewolfPlayer::createNewCapability);
    }

    public static <I extends IFactionPlayer<I>> IPlayableFaction<I> register(String name, Color color, int maxLevel, Class<I> icap, Class<? extends VampirismPlayer<I>> impl, Capability<I> cap, Function<PlayerEntity, ICapabilityProvider> provider) {
        ResourceLocation key = new ResourceLocation(Reference.MOD_ID, name);

        IPlayableFaction<I> faction = VampirismAPI.factionRegistry().registerPlayableFaction(
                key,
                icap,
                color,
                true,
                () -> cap,
                maxLevel
        );

        HelperRegistry.registerPlayerEventReceivingCapability(cap, impl);
        HelperRegistry.registerSyncablePlayerCapability(cap, key, impl);

        CAP_MAP.put(key, provider);

        return faction;
    }

    private static Map<ResourceLocation, Function<PlayerEntity, ICapabilityProvider>> CAP_MAP = new HashMap<>();

    @SubscribeEvent
    public static void onAttachEntity(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof PlayerEntity) {
            CAP_MAP.forEach((key, value) -> {
                e.addCapability(key, value.apply((PlayerEntity) e.getObject()));
            });
        }
    }
}
