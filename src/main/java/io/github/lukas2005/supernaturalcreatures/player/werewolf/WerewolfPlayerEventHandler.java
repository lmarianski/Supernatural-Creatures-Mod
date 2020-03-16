package io.github.lukas2005.supernaturalcreatures.player.werewolf;

import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import io.github.lukas2005.supernaturalcreatures.ModFactions;
import io.github.lukas2005.supernaturalcreatures.ModTags;
import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.potions.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber
public class WerewolfPlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END && !e.player.world.isRemote && FactionPlayerHandler.get(e.player).getCurrentFaction() == ModFactions.WEREWOLF_FACTION) {
            e.player.world.getProfiler().startSection(Reference.MOD_ID+".werewolf_tick");

            if (e.player.inventory.hasAny((Set<Item>) ModTags.WEREWOLF_POISON.getAllElements()) &&
                    e.player.getActivePotionEffect(ModEffects.SILVER_POISONING.get()) == null) {
                e.player.addPotionEffect(new EffectInstance(ModEffects.SILVER_POISONING.get(), 500, 0, false, false));
            }
            
            e.player.world.getProfiler().endSection();
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent e) {
        if (e.getEntity() instanceof PlayerEntity && !e.getEntity().world.isRemote
                && FactionPlayerHandler.get((PlayerEntity) e.getEntity()).getCurrentFaction() == ModFactions.WEREWOLF_FACTION) {
            if (e.getSource().getTrueSource() instanceof LivingEntity) {
                LivingEntity source = (LivingEntity) e.getSource().getTrueSource();

                if (ModTags.WEREWOLF_POISON.contains(source.getHeldItemMainhand().getItem())) {
                    e.getEntityLiving().addPotionEffect(new EffectInstance(ModEffects.SILVER_POISONING.get(), 250, 0, false, false));
                }
            }
        }
    }

}
