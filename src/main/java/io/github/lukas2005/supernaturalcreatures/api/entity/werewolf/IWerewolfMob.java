package io.github.lukas2005.supernaturalcreatures.api.entity.werewolf;

import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;

public interface IWerewolfMob extends IWerewolf {

    @Override
    default void setPackRank(EnumPackRank rank) {}

}
