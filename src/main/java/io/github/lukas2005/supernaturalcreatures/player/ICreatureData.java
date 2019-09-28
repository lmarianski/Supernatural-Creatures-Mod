package io.github.lukas2005.supernaturalcreatures.player;

import net.minecraft.nbt.INBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICreatureData {

    @Nonnull
    INBT toNBT();
    @Nonnull
    ICreatureData fromNBT(@Nullable INBT nbt);

}
