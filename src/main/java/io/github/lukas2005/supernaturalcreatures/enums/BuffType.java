package io.github.lukas2005.supernaturalcreatures.enums;

import net.minecraft.entity.ai.attributes.IAttribute;

import javax.annotation.Nullable;
import java.util.HashMap;

public enum BuffType {
	MAX_HEALTH("generic.maxHealth"),
	MOVEMENT_SPEED("generic.movementSpeed"),
	JUMP_HEIGHT,
	FALL_DISTANCE,
	ATTACK_DAMAGE("generic.attackDamage"),
	ATTACK_SPEED("generic.attackSpeed"),
	ATTACK_KNOCKBACK("generic.attackKnockback"),
	KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
	DAMAGE_RESISTANCE,
	BREAK_SPEED;

	private static HashMap<String, BuffType> modToType = new HashMap<>();
	static {
		for (BuffType type : BuffType.values()) {
			if (type.attributeName != null)
				modToType.put(type.attributeName, type);
		}
	}

	String attributeName;

	BuffType() {
		attributeName = null;
	}

	BuffType(String attributeName) {
		this.attributeName = attributeName;
	}

	@Nullable
	public static BuffType byAttribute(String attribute) {
		return modToType.getOrDefault(attribute, null);
	}

	@Nullable
	public static BuffType byAttribute(IAttribute attribute) {
		return byAttribute(attribute.getName());
	}
}
