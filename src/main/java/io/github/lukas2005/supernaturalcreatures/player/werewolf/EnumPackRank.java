package io.github.lukas2005.supernaturalcreatures.player.werewolf;

public enum EnumPackRank {
    ALPHA,
    BETA,
    OMEGA;

    public static EnumPackRank byOrdinal(int ordinal) {
        return values()[ordinal];
    }
}
