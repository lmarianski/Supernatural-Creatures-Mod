package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.potions.ModEffects;
import io.github.lukas2005.supernaturalcreatures.world.PackHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OdlEventHandler {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntity();
            if (!e.getWorld().isRemote) {
 //               SCMPlayer.of(player).syncData(true);
            }
            //AttributeModifier.applyModifiers(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
//            if (e.player.isAlive()) {
//                SCMPlayer playerData = SCMPlayer.of(e.player);
//
//                CreatureType type = playerData.getCreatureType();

//				for (Skill skill : playerData.getHighestLevelSkills(true)) {
//					if (skill != null) {
//						skill.onPlayerTick(e, playerData);
//					}
//				}

//                type.getBehaviour().onPlayerTick(e, playerData);
//            }
        }
    }

    @SubscribeEvent
    public static void onBlockUse(PlayerInteractEvent.RightClickBlock e) {
        if (e.getWorld().getBlockState(e.getPos()).getBlock() instanceof BedBlock) {
            EffectInstance effect = e.getEntityLiving().getActivePotionEffect(ModEffects.WAKEFULNESS.get());
            if (effect != null) {
                e.setCanceled(true);
                if (e.getEntityLiving() instanceof PlayerEntity) {
                    ((PlayerEntity) e.getEntityLiving()).sendStatusMessage(new TranslationTextComponent("effect.wakefulness.bed").applyTextStyles(TextFormatting.GRAY, TextFormatting.ITALIC), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Start event) {
        EffectInstance instance = event.getEntityLiving().getActivePotionEffect(ModEffects.FOOD_INDIGESTION.get());
        if (event.getEntityLiving() instanceof PlayerEntity) {
//            SCMPlayer player = SCMPlayer.of((PlayerEntity) event.getEntityLiving());
//            ItemStack stack = event.getItem();
//            if (instance != null) {
//                if (stack.getItem().isFood()) {
//                    switch (player.getConversion()) {
//                        case HUMAN:
//                            break;
//                        case WEREWOLF:
//                            if (stack.getItem().getFood().isMeat()) {
//                                break;
//                            }
//                        case VAMPIRE:
//                            event.setCanceled(true);
//                            break;
//                    }
//                }
//            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone e) {
        //WerewolfPlayer.of(e.getOriginal()).;
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent e) {
//        if (e.getEntityLiving() instanceof PlayerEntity) {
//            PlayerEntity player = (PlayerEntity) e.getEntityLiving();
//            SCMPlayer playerData = SCMPlayer.of(player);
//
//            if (playerData.getConversion() == CreatureType.VAMPIRE) {
//                if (!e.getSource().isFireDamage()) {
//                    e.setCanceled(true);
//                    player.setHealth(player.getMaxHealth());
//
//                    if (!e.getEntityLiving().getEntityWorld().isRemote) {
//                        ITextComponent msg = e.getSource().getDeathMessage(e.getEntityLiving());
//                        e.getEntityLiving().getServer().getPlayerList().sendMessage(msg);
//
//                        //((PlayerEntity) e.getEntityLiving()).sendStatusMessage(new StringTextComponent("You have arisen as a vampire!").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC), false);
//
//                        if (e.getEntityLiving().getServer().getPlayerList().getCurrentPlayerCount() == 1) {
//                            long time = e.getEntityLiving().getEntityWorld().getDayTime();
//
//                            e.getEntityLiving().getEntityWorld().setDayTime(time + Utils.timeUntilNextFullMoon(time));
//                        }
//                    }
//
//                    ConvertManager.endInfection(player, true);
//                } else {
//                    ConvertManager.endInfection(player, false);
//                }
//            }
//
//        }
    }

    public static void addPotion(LivingEntity e, Effect potion, int duration, int amplifier, boolean ambient, boolean showParticles) {
        if (!e.isPotionActive(potion))
            e.addPotionEffect(new EffectInstance(potion, duration, amplifier, ambient, showParticles));
    }

    public static void curePotion(LivingEntity e, Effect potion) {
        if (e.isPotionActive(potion)) {
            e.removePotionEffect(potion);

//			try {
//				Class<? extends LivingEntity> entityClass = e.getClass();
//				Field f = Utils.getField(entityClass, "potionsNeedUpdate");
//				f.setAccessible(true);
//				f.set(e, true);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
            //e.tick();
        }
    }
}
