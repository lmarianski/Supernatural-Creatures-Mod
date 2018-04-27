package io.github.lukas2005.supernaturalcreatures.enums;

import io.github.lukas2005.supernaturalcreatures.behaviour.CreatureBehaviour;
import io.github.lukas2005.supernaturalcreatures.behaviour.HumanBehaviour;
import io.github.lukas2005.supernaturalcreatures.behaviour.VampireBehaviour;
import io.github.lukas2005.supernaturalcreatures.behaviour.WerewolfBehaviour;
import io.github.lukas2005.supernaturalcreatures.skill.SkillTree;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public enum CreatureType {

	HUMAN,
	VAMPIRE(new VampireBehaviour(), 2f, 0f, 0f, 0f, 0f, 5f, 1.5f, 4f, 1.5f, false),
	WEREWOLF(new WerewolfBehaviour(), 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, true);

	private CreatureBehaviour behaviour;
	public float baseSpeed = 0;
	public float baseDigSpeed = 0;
	public float baseHealth = 0;
	public float baseKnockback = 0;
	public float baseKnockbackResistance = 0;
	public float baseStrength = 0;
	public float baseJumpHeight = 0;
	public float baseResistance = 0;
	public float baseFallDistance = 0;


	public boolean doesTransform = false;

	CreatureType() {
		behaviour = new HumanBehaviour();
	}

	CreatureType(CreatureBehaviour behaviour, float baseSpeed, float baseDigSpeed, float baseHealth, float baseKnockback, float baseKnockbackResistance, float baseStrength, float baseJumpHeight, float baseResistance, float baseFallDistance, boolean doesTransform) {
		this.baseSpeed = baseSpeed;
		this.baseDigSpeed = baseDigSpeed;
		this.baseHealth = baseHealth;
		this.baseKnockback = baseKnockback;
		this.baseKnockbackResistance = baseKnockbackResistance;
		this.baseStrength = baseStrength;
		this.baseJumpHeight = baseJumpHeight;
		this.baseResistance = baseResistance;
		this.baseFallDistance = baseFallDistance;
		this.doesTransform = doesTransform;
		this.behaviour = behaviour;
	}

	public static CreatureType byOrdinal(int ordinal) {
		return values()[ordinal];
	}

	public CreatureBehaviour getBehaviour() {
		return behaviour;
	}
}
