package io.github.lukas2005.supernaturalcreatures.api.entity.werewolf;

import de.teamlapen.vampirism.api.difficulty.IAdjustableLevel;
import de.teamlapen.vampirism.api.entity.IVillageCaptureEntity;
import io.github.lukas2005.supernaturalcreatures.api.entity.IMinion;
import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;

public interface IOmegaWerewolf extends IWerewolfMob, IAdjustableLevel, IMinion<IBetaWerewolf, IOmegaWerewolf>, IVillageCaptureEntity {

    @Override
    default EnumPackRank getPackRank() {
        return EnumPackRank.OMEGA;
    }


}
