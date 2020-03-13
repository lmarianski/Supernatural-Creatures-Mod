package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.entity.ModEntities;
import io.github.lukas2005.supernaturalcreatures.player.ConvertManager;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.enums.BuffType;
import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import io.github.lukas2005.supernaturalcreatures.potions.ModEffects;
import io.github.lukas2005.supernaturalcreatures.world.Pack;
import io.github.lukas2005.supernaturalcreatures.world.PackHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntity();
            if (!e.getWorld().isRemote) {
                SCMPlayer.of(player).syncData(true);
            }
            AttributeModifier.applyModifiers(player);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e) {
        if (e.getWorld() != null && !e.getWorld().isRemote()) {
            PackHandler handler = PackHandler.forWorld((World) e.getWorld());

            if (!handler.closed.contains(e.getChunk().getPos())) {
                handler.enqueue((Chunk) e.getChunk());
            }
        }

//        World world = (World) e.getWorld();
//        ChunkPos pos = e.getChunk().getPos();
//
//        ChunkPos[] arr = {
//                new ChunkPos(pos.x + 1, pos.z),
//                new ChunkPos(pos.x - 1, pos.z),
//                new ChunkPos(pos.x, pos.z + 1),
//                new ChunkPos(pos.x, pos.z - 1),
//                new ChunkPos(pos.x + 1, pos.z + 1),
//                new ChunkPos(pos.x - 1, pos.z - 1),
//                new ChunkPos(pos.x + 1, pos.z - 1),
//                new ChunkPos(pos.x - 1, pos.z + 1),
//        };
//
//        if (world != null && !world.isRemote() && world.getDimension().isSurfaceWorld()) {
//            PackHandler handler = PackHandler.forWorld(world);
//
//            boolean flag = false;
//
//            for (Biome b : e.getChunk().getBiomes()) {
//                if (ModEntities.wolfBiomes.contains(b)) {
//                    flag = true;
//                    break;
//                }
//            }
//
//            if (!handler.closed.contains(pos) && flag) {
//                if (!handler.isOccupied(pos)) {
//                    if (handler.isRadiusClear(pos, 10)) {
//                        if (world.getRandom().nextFloat() >= 0.99) {
//                            handler.newPack((Chunk) e.getChunk());
//                        }
//                    } else {
//                        Pack pack = null;
//
//                        if (handler.isOccupied(arr[0])) {
//                            pack = handler.getPackAt(arr[0]);
//                        } else if (handler.isOccupied(arr[1])) {
//                            pack = handler.getPackAt(arr[1]);
//                        } else if (handler.isOccupied(arr[2])) {
//                            pack = handler.getPackAt(arr[2]);
//                        } else if (handler.isOccupied(arr[3])) {
//                            pack = handler.getPackAt(arr[3]);
//                        }
//
//                        if (pack != null) {
//                            handler.growPack(pack, (Chunk) e.getChunk());
//                        }
//                    }
//                }
//                handler.closed.add(pos);
//                handler.sync();
//            }
//        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            if (e.player.isAlive()) {
                SCMPlayer playerData = SCMPlayer.of(e.player);

                CreatureType type = playerData.getCreatureType();

//				for (Skill skill : playerData.getHighestLevelSkills(true)) {
//					if (skill != null) {
//						skill.onPlayerTick(e, playerData);
//					}
//				}

                type.getBehaviour().onPlayerTick(e, playerData);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockUse(PlayerInteractEvent.RightClickBlock e) {
        if (e.getWorld().getBlockState(e.getPos()).getBlock() instanceof BedBlock) {
            EffectInstance effect = e.getEntityLiving().getActivePotionEffect(ModEffects.WAKEFULNESS);
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
        EffectInstance instance = event.getEntityLiving().getActivePotionEffect(ModEffects.FOOD_INDIGESTION);
        if (event.getEntityLiving() instanceof PlayerEntity) {
            SCMPlayer player = SCMPlayer.of((PlayerEntity) event.getEntityLiving());
            ItemStack stack = event.getItem();
            if (instance != null) {
                if (stack.getItem().isFood()) {
                    switch (player.getConversion()) {
                        case HUMAN:
                            break;
                        case WEREWOLF:
                            if (stack.getItem().getFood().isMeat()) {
                                break;
                            }
                        case VAMPIRE:
                            event.setCanceled(true);
                            break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerBreakSpeed(PlayerEvent.BreakSpeed e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            SCMPlayer playerData = SCMPlayer.of(e.getPlayer());

            CreatureType type = playerData.getCreatureType();

            if (type.getBehaviour().shouldApplyBuff(BuffType.BREAK_SPEED, playerData, type))
                e.setNewSpeed((float) (e.getOriginalSpeed() + type.getBehaviour().getBuff(BuffType.BREAK_SPEED, playerData)));
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent e) {
        SCMPlayer attackerData = null;
        SCMPlayer victimData = null;

        if (e.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getSource().getTrueSource();

            attackerData = SCMPlayer.of(player);
            //CreatureType type = attackerData.getCreatureType();

            //if (type.getBehaviour().shouldApplyBuff(BuffType.ATTACK_DAMAGE, player, attackerData, type))
            //	e.setAmount(e.getAmount() + type.baseStrength * type.getBehaviour().getMultiplierForBuf(BuffType.ATTACK_DAMAGE, player, attackerData, attackerData.getLevel()));

//			for (Skill skill : attackerData.getHighestLevelSkills(false)) {
//				skill.onPlayerAttack(e, player, attackerData, e.getEntityLiving());
//			}
        }
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();

            victimData = SCMPlayer.of(player);
            CreatureType type = victimData.getCreatureType();

            if (type.getBehaviour().shouldApplyBuff(BuffType.DAMAGE_RESISTANCE, victimData, type)) {
                //TODO Damage Resistance
            }

            if (e.getSource() == DamageSource.FALL) {
                if (type.getBehaviour().shouldApplyBuff(BuffType.FALL_DISTANCE, victimData, type))
                    e.setAmount((float) (e.getAmount() - type.getBehaviour().getBuff(BuffType.FALL_DISTANCE, victimData) * 2));
            }

//			for (Skill skill : victimData.getHighestLevelSkills(false)) {
//				skill.onPlayerHurt(e, (LivingEntity) e.getSource().getTrueSource(), player, victimData);
//			}
        }
//		if (e.getSource().getTrueSource() instanceof PlayerEntity && e.getEntityLiving() instanceof PlayerEntity) {
//			PlayerEntity attacker = (PlayerEntity) e.getSource().getTrueSource();
//			PlayerEntity victim = (PlayerEntity) e.getEntityLiving();
//
//			for (Skill skill : attackerData.getHighestLevelSkills(false)) {
//				skill.onPlayerHurt(e, (LivingEntity) e.getSource().getTrueSource(), attacker, attackerData);
//			}
//
//			for (Skill skill : victimData.getHighestLevelSkills(false)) {
//				skill.onPlayerHurt(e, (LivingEntity) e.getSource().getTrueSource(), victim, victimData);
//			}
//		}
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();

            SCMPlayer playerData = SCMPlayer.of(player);

            CreatureType type = playerData.getCreatureType();

            if (type.getBehaviour().shouldApplyBuff(BuffType.JUMP_HEIGHT, playerData, type))
                player.addVelocity(0, (type.getBehaviour().getBuff(BuffType.JUMP_HEIGHT, playerData) + 0.5f) * 0.1F, 0);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone e) {
        SCMPlayer oldPlayer = SCMPlayer.of(e.getOriginal());
        SCMPlayer newPlayer = SCMPlayer.of(e.getPlayer());

        oldPlayer.cloneTo(newPlayer);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();
            SCMPlayer playerData = SCMPlayer.of(player);

            if (playerData.getConversion() == CreatureType.VAMPIRE) {
                if (!e.getSource().isFireDamage()) {
                    e.setCanceled(true);
                    player.setHealth(player.getMaxHealth());

                    if (!e.getEntityLiving().getEntityWorld().isRemote) {
                        ITextComponent msg = e.getSource().getDeathMessage(e.getEntityLiving());
                        e.getEntityLiving().getServer().getPlayerList().sendMessage(msg);

                        //((PlayerEntity) e.getEntityLiving()).sendStatusMessage(new StringTextComponent("You have arisen as a vampire!").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC), false);

                        if (e.getEntityLiving().getServer().getPlayerList().getCurrentPlayerCount() == 1) {
                            long time = e.getEntityLiving().getEntityWorld().getDayTime();

                            e.getEntityLiving().getEntityWorld().setDayTime(time + Utils.timeUntilNextFullMoon(time));
                        }
                    }

                    ConvertManager.endInfection(player, true);
                } else {
                    ConvertManager.endInfection(player, false);
                }
            }

        }
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
