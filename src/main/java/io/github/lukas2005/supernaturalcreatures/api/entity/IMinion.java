package io.github.lukas2005.supernaturalcreatures.api.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMinion<L extends IMinionLeader<L, M>, M extends IMinion<L, M>> {

    @Nullable
    L getLeader();
    void setLeader(L leader);

}
