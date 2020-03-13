package io.github.lukas2005.supernaturalcreatures.potions;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectBase extends Effect {

	public EffectBase(String name, EffectType type, int liquidColorIn) {
		super(type, liquidColorIn);
		setRegistryName(Reference.MOD_ID, name);
		//setPotionName("effect."+Reference.MOD_ID+"."+name);
	}

}
