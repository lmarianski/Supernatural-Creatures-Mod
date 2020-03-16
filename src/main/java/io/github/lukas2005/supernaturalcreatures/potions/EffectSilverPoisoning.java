package io.github.lukas2005.supernaturalcreatures.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

import javax.annotation.Nullable;

public class EffectSilverPoisoning extends EffectBase {
    public EffectSilverPoisoning(EffectType type, int liquidColorIn) {
        super(type, liquidColorIn);
    }

    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
        super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
    }
}
