package io.github.lukas2005.supernaturalcreatures.potions;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;

public class EffectBase extends Effect {

	public EffectBase(String name, EffectType type, int liquidColorIn) {
		super(type, liquidColorIn);
		setRegistryName(Reference.MOD_ID, name);
		//setPotionName("effect."+Reference.MOD_ID+"."+name);
	}

}
