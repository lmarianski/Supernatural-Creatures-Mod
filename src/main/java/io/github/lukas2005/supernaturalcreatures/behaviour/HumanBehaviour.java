package io.github.lukas2005.supernaturalcreatures.behaviour;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.enums.BufType;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.enums.ResistanceLevel;
import io.github.lukas2005.supernaturalcreatures.skill.SkillTree;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class HumanBehaviour extends DefaultCreatureBehaviour {

	@Override
	public boolean shouldApplyBuf(BufType buf, EntityPlayer player, IPlayerDataCapability playerData, CreatureType type) {
		return false;
	}

	@Override
	public ResistanceLevel getResistanceLevel(DamageSource source) {
		return ResistanceLevel.NONE;
	}
}
