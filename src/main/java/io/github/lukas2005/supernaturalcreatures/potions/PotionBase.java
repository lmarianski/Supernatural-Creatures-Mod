package io.github.lukas2005.supernaturalcreatures.potions;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.potion.Potion;

public class PotionBase extends Potion {

	public PotionBase(String name, boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
		setRegistryName(Reference.MOD_ID, name);
		setPotionName("effect."+Reference.MOD_ID+"."+name);
	}

}
