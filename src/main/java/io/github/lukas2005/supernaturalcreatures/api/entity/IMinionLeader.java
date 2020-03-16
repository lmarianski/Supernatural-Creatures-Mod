package io.github.lukas2005.supernaturalcreatures.api.entity;

import java.util.List;

public interface IMinionLeader<L extends IMinionLeader<L, M>, M extends IMinion<L, M>> {

    boolean addFollower(M follower);
    void removeFollower(M follower);

    int getFollowingCount();
    int getMaxFollowerCount();

    List<M> getFollowers();

}
