package io.github.lukas2005.supernaturalcreatures.api.entity.werewolf;

import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.actions.IActionHandler;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import io.github.lukas2005.supernaturalcreatures.enums.EnumForm;
import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;
import io.github.lukas2005.supernaturalcreatures.world.Pack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface IWerewolfPlayer extends IFactionPlayer<IWerewolfPlayer>, IWerewolf {

    ResourceLocation getSkin();

    void setSkin(ResourceLocation location);

    class Impl implements IWerewolfPlayer {

        @Override
        public boolean canLeaveFaction() {
            return false;
        }

        @Nullable
        @Override
        public IFaction getDisguisedAs() {
            return null;
        }

        @Override
        public IPlayableFaction<IWerewolfPlayer> getFaction() {
            return null;
        }

        @Override
        public LivingEntity getRepresentingEntity() {
            return null;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public int getMaxLevel() {
            return 0;
        }

        @Override
        public Predicate<LivingEntity> getNonFriendlySelector(boolean otherFactionPlayers, boolean ignoreDisguise) {
            return null;
        }

        @Override
        public IActionHandler<IWerewolfPlayer> getActionHandler() {
            return null;
        }

        @Override
        public PlayerEntity getRepresentingPlayer() {
            return null;
        }

        @Override
        public boolean isDisguised() {
            return false;
        }

        @Override
        public boolean isRemote() {
            return false;
        }

        @Override
        public void onLevelChanged(int newLevel, int oldLevel) {

        }

        @Override
        public ISkillHandler<IWerewolfPlayer> getSkillHandler() {
            return null;
        }

        @Override
        public Pack getPack() {
            return null;
        }

        @Override
        public EnumPackRank getPackRank() {
            return null;
        }

        @Override
        public void setPackRank(EnumPackRank rank) {

        }

        @Override
        public void setForm(EnumForm form) {

        }

        @Override
        public boolean isTransformed() {
            return false;
        }

        @Override
        public ResourceLocation getSkin() {
            return null;
        }

        @Override
        public void setSkin(ResourceLocation location) {

        }
    }

}
