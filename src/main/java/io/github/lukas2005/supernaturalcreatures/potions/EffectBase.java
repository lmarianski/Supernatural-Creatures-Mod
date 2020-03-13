package io.github.lukas2005.supernaturalcreatures.potions;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectBase extends Effect {

	public EffectBase(EffectType type, int liquidColorIn) {
		super(type, liquidColorIn);
		//setPotionName("effect."+Reference.MOD_ID+"."+name);
	}

}
