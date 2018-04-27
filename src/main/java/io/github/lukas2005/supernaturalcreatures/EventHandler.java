package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.enums.BufType;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.skill.Skill;
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
				e.player.setAIMoveSpeed(e.player.getAIMoveSpeed() + type.baseSpeed * type.getBehaviour().getMultiplierForBuf(BufType.SPEED_BOOST, e.player, playerData, playerData.getLevel()));

			for (Skill skill : playerData.getHighestLevelSkills(true)) {
				if (skill != null) {
					skill.onPlayerTick(e, playerData);
				}
			}

			type.getBehaviour().onPlayerTick(e, playerData);
		}
	}

	@SubscribeEvent
	public static void playerBreakSpeed(PlayerEvent.BreakSpeed e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			IPlayerDataCapability playerData = e.getEntityPlayer().getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			CreatureType type = playerData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.DIG_SPEED_BOOST, e.getEntityPlayer(), playerData, type))
				e.setNewSpeed(e.getOriginalSpeed() + playerData.getCreatureType().baseDigSpeed * type.getBehaviour().getMultiplierForBuf(BufType.DIG_SPEED_BOOST, e.getEntityPlayer(), playerData, playerData.getLevel()));
		}
	}

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent e) {
		IPlayerDataCapability attackerData = null;
		IPlayerDataCapability victimData = null;

		if (e.getSource().getSourceOfDamage() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getSource().getSourceOfDamage();

			attackerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
			CreatureType type = attackerData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.STRENGTH_BOOST, player, attackerData, type))
				e.setAmount(e.getAmount() + type.baseStrength * type.getBehaviour().getMultiplierForBuf(BufType.STRENGTH_BOOST, player, attackerData, attackerData.getLevel()));

			for (Skill skill : attackerData.getHighestLevelSkills(false)) {
				skill.onPlayerAttack(e, player, attackerData, e.getEntityLiving());
			}
		}
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();

			victimData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			CreatureType type = victimData.getCreatureType();

			if (type.getBehaviour().shouldApplyBuf(BufType.RESISTANCE, player, victimData, type)) {
				switch (type.getBehaviour().getResistanceLevel(e.getSource())) {
					case NONE:
						break;
					case RESISTANT:
						e.setAmount(e.getAmount() - type.baseResistance * type.getBehaviour().getMultiplierForBuf(BufType.RESISTANCE, player, victimData, victimData.getLevel()));
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
					type.getBehaviour().applyWeakness(e, victimData, type, player);
					break;
			}

			if (e.getSource() == DamageSource.fall) {
				if (type.getBehaviour().shouldApplyBuf(BufType.FALL_DISTANCE, player, victimData, type))
					e.setAmount(e.getAmount() - type.baseFallDistance * type.getBehaviour().getMultiplierForBuf(BufType.FALL_DISTANCE, player, victimData, victimData.getLevel()) * 2);
			}

			for (Skill skill : victimData.getHighestLevelSkills(false)) {
				skill.onPlayerHurt(e, (EntityLivingBase) e.getSource().getSourceOfDamage(), player, victimData);
			}
		}
		if (e.getSource().getSourceOfDamage() instanceof EntityPlayer && e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer attacker = (EntityPlayer) e.getSource().getSourceOfDamage();
			EntityPlayer victim = (EntityPlayer) e.getEntityLiving();

			for (Skill skill : attackerData.getHighestLevelSkills(false)) {
				skill.onPlayerHurt(e, (EntityLivingBase) e.getSource().getSourceOfDamage(), attacker, attackerData);
			}


			for (Skill skill : victimData.getHighestLevelSkills(false)) {
				skill.onPlayerHurt(e, (EntityLivingBase) e.getSource().getSourceOfDamage(), victim, victimData);
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
				if (type.baseJumpHeight > 0)
					player.addVelocity(0, (type.baseJumpHeight * type.getBehaviour().getMultiplierForBuf(BufType.JUMP_BOOST, player, playerData, playerData.getLevel()) + 0.5f) * 0.1F, 0);
			//player.jump(); // Never ever uncomment this
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone e) {
		EntityPlayer newPlayer = e.getEntityPlayer();
		EntityPlayer oldPlayer = e.getOriginal();


		IPlayerDataCapability newPlayerData = newPlayer.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
		IPlayerDataCapability oldPlayerData = oldPlayer.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

		oldPlayerData.cloneTo(newPlayerData);
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
