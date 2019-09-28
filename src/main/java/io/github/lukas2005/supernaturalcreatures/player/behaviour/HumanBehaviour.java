package io.github.lukas2005.supernaturalcreatures.player.behaviour;

import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import io.github.lukas2005.supernaturalcreatures.player.ICreatureData;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.enums.BuffType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HumanBehaviour extends DefaultCreatureBehaviour {

	@Override
	public boolean shouldApplyBuff(BuffType buff, SCMPlayer playerData, CreatureType type) {
		return false;
	}

}
