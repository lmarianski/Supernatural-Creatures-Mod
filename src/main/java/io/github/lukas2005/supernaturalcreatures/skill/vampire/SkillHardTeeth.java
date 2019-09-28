package io.github.lukas2005.supernaturalcreatures.skill.vampire;

import io.github.lukas2005.supernaturalcreatures.DamageSources;
import io.github.lukas2005.supernaturalcreatures.player.capability.IPlayerData;
import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SkillHardTeeth extends Skill {

	int lvl;

	public SkillHardTeeth(int lvl) {
		super("hardTeethLvl"+lvl);
		setCost(lvl+2*lvl-1);
		this.lvl = lvl;
	}

	@Override
	public void onPlayerAttack(LivingHurtEvent e, PlayerEntity attacker, IPlayerData attackerData, LivingEntity victim) {
		super.onPlayerAttack(e, attacker, attackerData, victim);
		if (DamageSources.areEqual(e.getSource(), DamageSources.VAMPIRE_BITE)) {
			e.setAmount(e.getAmount()+3*lvl);
			if (lvl >= 3) {
				if (lvl >= 4 && attacker.isSneaking()) {
					e.setAmount(0);

					//TODO SanguinarePotion.addRandom(victim);
				} else {
					victim.addPotionEffect(new EffectInstance(Effects.POISON, 60, 100, false, false));
				}
			}
		}
	}
}
