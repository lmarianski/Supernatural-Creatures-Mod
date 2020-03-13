package io.github.lukas2005.supernaturalcreatures.api.entity.werewolf;

import de.teamlapen.vampirism.api.entity.factions.IFactionEntity;
import io.github.lukas2005.supernaturalcreatures.enums.EnumForm;
import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;
import io.github.lukas2005.supernaturalcreatures.world.Pack;

public interface IWerewolf extends IFactionEntity {

    Pack getPack();
    EnumPackRank getPackRank();

    default EnumForm getForm() {
        return EnumForm.HUMAN;
    }

    boolean isTransformed();
}
