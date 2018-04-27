package io.github.lukas2005.supernaturalcreatures.potions;

import io.github.lukas2005.supernaturalcreatures.Utils;
import io.github.lukas2005.supernaturalcreatures.behaviour.VampireBehaviour;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.awt.*;

public class SanguinarePotion extends PotionBase {
	public SanguinarePotion() {
		super("sanguinare", true, new Color(52, 0, 64).getRGB());
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration == 2;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int p_76394_2_) {
		if (entity instanceof EntityPlayer) {
			VampireBehaviour.turnPlayerIntoVampire((EntityPlayer) entity);
		} else {
			VampireBehaviour.turnIntoVampire(entity);
		}
	}

	/**
	 * @param entity
	 */

	public static void addRandom(EntityLivingBase entity) {
		int avgDuration = 20 * (entity instanceof EntityPlayer ? 10 : 5);
		int duration = (int) ((entity.getRNG().nextFloat() + 0.5F) * avgDuration);
		PotionEffect effect = new Effect(ModPotions.SANGUINARE, duration);
//		if (!) {
//			effect.setCurativeItems(new ArrayList<>());
//		}
		entity.addPotionEffect(effect);
	}

	public static class Effect extends PotionEffect {

		public Effect(Potion potionIn, int duration) {
			super(potionIn, duration);
		}

		@Override
		public boolean onUpdate(EntityLivingBase entityIn) {
			if (this.getDuration() % 10 == 0) {
				if (!VampireBehaviour.canBecomeVampire(entityIn)) {
					return false;
				}
			}
			return super.onUpdate(entityIn);
		}
	}
}
