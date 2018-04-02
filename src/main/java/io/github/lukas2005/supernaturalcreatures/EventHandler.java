package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.enums.BufType;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class EventHandler {

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity();
			if (!e.getWorld().isRemote) {
				IPlayerDataCapability playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
				if (!playerData.getDataMap().containsKey("fangs") || !playerData.getDataMap().containsKey("eyes")) {
					playerData.setData("vampire.fangs", "0");
					playerData.setData("vampire.eyes", "0");
				}
				playerData.syncData(player);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
		if (e.phase == TickEvent.Phase.END) {
			IPlayerDataCapability playerData = e.player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			CreatureType type = playerData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.HEALTH_BOOST, e.player, playerData, type)) {
				//e.player.setHealth(e.player.getMaxHealth()+type.baseHealth);
				addPotion(e.player, MobEffects.HEALTH_BOOST, Integer.MAX_VALUE, (int) type.baseHealth, true, false);
			} else {
				curePotion(e.player, MobEffects.HEALTH_BOOST);
			}

			if (type.getBehaviour().shouldApplyBuf(BufType.SPEED_BOOST, e.player, playerData, type))
				e.player.setAIMoveSpeed(e.player.getAIMoveSpeed() + type.baseSpeed);
		}
	}

	@SubscribeEvent
	public static void playerBreakSpeed(PlayerEvent.BreakSpeed e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			IPlayerDataCapability playerData = e.getEntityPlayer().getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			CreatureType type = playerData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.DIG_SPEED_BOOST, e.getEntityPlayer(), playerData, type))
				e.setNewSpeed(e.getOriginalSpeed() + playerData.getCreatureType().baseDigSpeed);
		}
	}

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent e) {
		if (e.getSource().getSourceOfDamage() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getSource().getSourceOfDamage();

			IPlayerDataCapability playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
			CreatureType type = playerData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.STRENGTH_BOOST, player, playerData, type))
				e.setAmount(e.getAmount() + type.baseStrength);
		}
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();

			IPlayerDataCapability playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			CreatureType type = playerData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.RESISTANCE, player, playerData, type)) {
				switch (type.getBehaviour().getResistanceLevel(e.getSource())) {
					case NONE:
						break;
					case RESISTANT:
						e.setAmount(e.getAmount() - type.baseResistance);
						break;
					case INVULNERABLE:
						e.setCanceled(true);
						break;
				}
			}
			switch (type.getBehaviour().getResistanceLevel(e.getSource())) {
				case INSTANT_DEATH:
					e.setAmount(Integer.MAX_VALUE);
					break;
				case WEAKNESS:
					type.getBehaviour().applyWeakness(e, playerData, type, player);
					break;
			}

			if (e.getSource() == DamageSource.fall) {
				if (type.getBehaviour().shouldApplyBuf(BufType.FALL_DISTANCE, player, playerData, type))
					type.baseFallDistance = 1.5f;
				e.setAmount(e.getAmount() - type.baseFallDistance * 2);
			}
		}

	}

	@SubscribeEvent
	public static void onJump(LivingEvent.LivingJumpEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();

			IPlayerDataCapability playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			CreatureType type = playerData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.JUMP_BOOST, player, playerData, type))
				if (type.baseJumpHeight > 0) player.addVelocity(0, (type.baseJumpHeight + 0.5f) * 0.1F, 0);
			//player.jump(); // Never ever uncomment this
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone e) {
		if (e.isWasDeath()) {
			EntityPlayer newPlayer = e.getEntityPlayer();
			EntityPlayer oldPlayer = e.getOriginal();


			IPlayerDataCapability newPlayerData = newPlayer.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
			IPlayerDataCapability oldPlayerData = oldPlayer.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			oldPlayerData.cloneTo(newPlayerData);
		}
	}

	public static void addPotion(EntityLivingBase e, Potion potion, int duration, int amplifier, boolean ambient, boolean showParticles) {
		if (!e.isPotionActive(potion))
			e.addPotionEffect(new PotionEffect(potion, duration, amplifier, ambient, showParticles));
	}

	public static void curePotion(EntityLivingBase e, Potion potion) {
		if (e.isPotionActive(potion)) {
			e.removePotionEffect(potion);

			try {
				Class<? extends EntityLivingBase> entityClass = e.getClass();
				Field f = Utils.getField(entityClass, "potionsNeedUpdate");
				f.setAccessible(true);
				f.set(e, true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.onEntityUpdate();
		}
	}
}
