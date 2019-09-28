package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class DamageSources {

	public static final DamageSource VAMPIRE_BITE = new DamageSource(Reference.MOD_ID+".vampire_bite");

	public static final DamageSource SUN = new DamageSource(Reference.MOD_ID+".sun").setDamageBypassesArmor().setDifficultyScaled().setFireDamage();

	public static DamageSource vampireBite(Entity attacker) {
		return new EntityDamageSource(VAMPIRE_BITE.getDamageType(), attacker);
	}

	public static boolean areEqual(DamageSource source1, DamageSource source2) {
		return source1.damageType.equals(source2.damageType);
	}
}
