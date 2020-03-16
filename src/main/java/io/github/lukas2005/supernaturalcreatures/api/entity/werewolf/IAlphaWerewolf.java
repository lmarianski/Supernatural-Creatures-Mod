package io.github.lukas2005.supernaturalcreatures.api.entity.werewolf;

import de.teamlapen.vampirism.api.difficulty.IAdjustableLevel;
import de.teamlapen.vampirism.api.entity.IVillageCaptureEntity;
import io.github.lukas2005.supernaturalcreatures.api.entity.IMinionLeader;
import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;

public interface IAlphaWerewolf extends IWerewolfMob, IMinionLeader<IAlphaWerewolf, IBetaWerewolf>, IAdjustableLevel, IVillageCaptureEntity {

    @Override
    default EnumPackRank getPackRank() {
        return EnumPackRank.ALPHA;
    }


}
