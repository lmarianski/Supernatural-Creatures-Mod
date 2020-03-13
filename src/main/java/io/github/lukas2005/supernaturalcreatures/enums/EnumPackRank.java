package io.github.lukas2005.supernaturalcreatures.enums;

public enum EnumPackRank {
    ALPHA,
    BETA,
    OMEGA;

    public static EnumPackRank by(int ordinal) {
        return values()[ordinal];
    }
}
