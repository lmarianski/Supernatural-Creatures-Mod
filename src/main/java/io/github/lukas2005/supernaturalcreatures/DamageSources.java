package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class DamageSources {

	public static final DamageSource vampire_bite = new DamageSource(Reference.MOD_ID+".vampire_bite").setDamageBypassesArmor();

	public static DamageSource vampireBite(Entity attacker) {
		return new EntityDamageSource(vampire_bite.getDamageType(), attacker).setDamageBypassesArmor();
	}

	public static boolean areEqual(DamageSource source1, DamageSource source2) {
		return source1.damageType.equals(source2.damageType);
	}
}
