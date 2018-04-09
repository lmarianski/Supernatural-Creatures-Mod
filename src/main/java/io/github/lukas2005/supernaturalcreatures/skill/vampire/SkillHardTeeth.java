package io.github.lukas2005.supernaturalcreatures.skill.vampire;

import io.github.lukas2005.supernaturalcreatures.DamageSources;
import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SkillHardTeeth extends Skill {

	int lvl;

	public SkillHardTeeth(int lvl) {
		super("hardTeethLvl"+lvl);
		setCost(lvl+2*lvl-1);
		this.lvl = lvl;
	}

	@Override
	public void onPlayerAttack(LivingHurtEvent e, EntityPlayer attacker, IPlayerDataCapability attackerData, EntityLivingBase victim) {
		super.onPlayerAttack(e, attacker, attackerData, victim);
		if (DamageSources.areEqual(e.getSource(), DamageSources.vampire_bite)) {
			e.setAmount(e.getAmount()+3*lvl);
			if (lvl >= 3) {
				victim.addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 100, false, false));
			}
		}
	}
}
