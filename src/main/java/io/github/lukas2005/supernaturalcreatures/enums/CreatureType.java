package io.github.lukas2005.supernaturalcreatures.enums;

public enum CreatureType {

	HUMAN,
	VAMPIRE;

	public static CreatureType byOrdinal(int ordinal) {
		return values()[ordinal];
	}

}
