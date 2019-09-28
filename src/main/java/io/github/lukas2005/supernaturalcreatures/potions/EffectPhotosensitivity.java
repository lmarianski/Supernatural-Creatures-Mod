package io.github.lukas2005.supernaturalcreatures.potions;

import io.github.lukas2005.supernaturalcreatures.DamageSources;
import io.github.lukas2005.supernaturalcreatures.Utils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

public class EffectPhotosensitivity extends EffectBase {
    public EffectPhotosensitivity(String name, EffectType type, int liquidColorIn) {
        super(name, type, liquidColorIn);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int j = 25 >> amplifier;
        if (j > 0) {
            return duration % j == 0;
        } else {
            return true;
        }
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (entity.getHealth() > 1.0F && Utils.gettingSundamge(entity)) {
            entity.attackEntityFrom(DamageSources.SUN, 1.0F);
        }
    }

}
