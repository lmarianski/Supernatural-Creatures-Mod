package io.github.lukas2005.supernaturalcreatures.enums;

public enum EnumForm {

    HUMAN,
    FERAL,
    HYBRID;

    public static EnumForm by(int ordinal) {
        return values()[ordinal];
    }
}
