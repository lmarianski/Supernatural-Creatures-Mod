package io.github.lukas2005.supernaturalcreatures.player.werewolf;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.actions.IActionHandler;
import de.teamlapen.vampirism.api.entity.player.skills.ISkillHandler;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import de.teamlapen.vampirism.player.VampirismPlayer;
import de.teamlapen.vampirism.player.actions.ActionHandler;
import de.teamlapen.vampirism.player.skills.SkillHandler;
import io.github.lukas2005.supernaturalcreatures.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.ModFactions;
import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.api.entity.werewolf.IWerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.enums.EnumForm;
import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;
import io.github.lukas2005.supernaturalcreatures.world.Pack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class WerewolfPlayer extends VampirismPlayer<IWerewolfPlayer> implements IWerewolfPlayer {

    @CapabilityInject(IWerewolfPlayer.class)
    public static final Capability<IWerewolfPlayer> CAP = null;
    public static final ResourceLocation CAP_KEY = new ResourceLocation(Reference.MOD_ID, "werewolf");

    public static final int EYE_OVERLAY_COUNT = 3;
    public static final int SKIN_OVERLAY_COUNT = 4;

    public static List<ResourceLocation> skinOverlays = new ArrayList<>();
    public static List<ResourceLocation> eyeOverlays = new ArrayList<>();

    private final static String TAG = "WerewolfPlayer";

    private static final UUID HARVEST_SPEED = UUID.fromString("aa6a303b-c860-40c0-9ff1-a1e3c9e29c50");
    private static final UUID HARVEST_LEVEL = UUID.fromString("d95b9235-4857-4268-a971-4d8b4b53da66");


    /**
     * Don't call before the construction event of the player entity is finished
     */
    public static WerewolfPlayer of(PlayerEntity player) {
        return (WerewolfPlayer) player.getCapability(CAP, null).orElseThrow(() -> new IllegalStateException("Entity doesn't have capability!"));
    }

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(IWerewolfPlayer.class, new Storage(), IWerewolfPlayer.Impl::new);
    }

    public static ICapabilityProvider createNewCapability(final PlayerEntity player) {
        return new ICapabilitySerializable<CompoundNBT>() {

            final WerewolfPlayer inst = new WerewolfPlayer(player);
            final LazyOptional<IWerewolfPlayer> opt = LazyOptional.of(() -> inst);

            @Override
            public void deserializeNBT(CompoundNBT nbt) {
                CAP.getStorage().readNBT(CAP, inst, null, nbt);
            }

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
                return CAP.orEmpty(capability, opt);
            }

            @Override
            public CompoundNBT serializeNBT() {
                return (CompoundNBT) CAP.getStorage().writeNBT(CAP, inst, null);
            }
        };
    }

    private final ActionHandler<IWerewolfPlayer> actionHandler;
    private final SkillHandler<IWerewolfPlayer> skillHandler;
    private int waterTime = 0;
    private int killedAnimal = 0;
    private boolean sleep = false;

    private ResourceLocation skin = eyeOverlays.get(0);
    private EnumPackRank packRank = EnumPackRank.OMEGA;
    private EnumForm form = EnumForm.HUMAN;

    public WerewolfPlayer(PlayerEntity player) {
        super(player);
        this.applyEntityAttributes();
        this.actionHandler = new ActionHandler<>(this);
        this.skillHandler = new SkillHandler<>(this, ModFactions.WEREWOLF_FACTION);
    }

    public void turnWerewolf() {
        FactionPlayerHandler.get(player).joinFaction(ModFactions.WEREWOLF_FACTION);
    }

    private void applyEntityAttributes() {
        AbstractAttributeMap attrs = this.player.getAttributes();

        if (attrs.getAttributeInstance(Reference.biteDamage) == null) {
            attrs.registerAttribute(Reference.biteDamage);
        }
        if (attrs.getAttributeInstance(Reference.harvestSpeed) == null) {
            attrs.registerAttribute(Reference.harvestSpeed);
        }
        if (attrs.getAttributeInstance(Reference.harvestLevel) == null) {
            attrs.registerAttribute(Reference.harvestLevel);
        }
    }

    @Override
    public boolean canLeaveFaction() {
        return true;
    }

    @Override
    public IPlayableFaction<IWerewolfPlayer> getFaction() {
        return ModFactions.WEREWOLF_FACTION;
    }

    @Override
    public int getMaxLevel() {
        return 30;
    }

    @Override
    public Predicate<LivingEntity> getNonFriendlySelector(boolean otherFactionPlayers, boolean ignoreDisguise) {
        if (otherFactionPlayers)
            return entity -> true;
        else
            return VampirismAPI.factionRegistry().getPredicate(this.getFaction(), ignoreDisguise);
    }

    @Override
    public ISkillHandler<IWerewolfPlayer> getSkillHandler() {
        return skillHandler;
    }

    @Override
    public IActionHandler<IWerewolfPlayer> getActionHandler() {
        return actionHandler;
    }

    @Nullable
    @Override
    public IFaction getDisguisedAs() {
        return null;
    }

    @Override
    public boolean isDisguised() {
        return false;
    }

    @Override
    public EnumForm getForm() {
        return form;
    }

    @Override
    public void setForm(EnumForm form) {
        this.form = form;
    }

    @Override
    public boolean isTransformed() {
        return getForm() != EnumForm.HUMAN;
    }

    @Override
    public ResourceLocation getCapKey() {
        return CAP_KEY;
    }

    @Override
    public Pack getPack() {
        return null;
    }

    @Override
    public EnumPackRank getPackRank() {
        return packRank;
    }

    @Override
    public void setPackRank(EnumPackRank rank) {
        packRank = rank;
    }

    @Override
    public ResourceLocation getSkin() {
        return skin;
    }

    @Override
    public void setSkin(ResourceLocation location) {
        skin = location;
    }

    @Override
    protected WerewolfPlayer copyFromPlayer(PlayerEntity old) {
        WerewolfPlayer oldWerewolve = of(old);
        CompoundNBT nbt = new CompoundNBT();
        oldWerewolve.saveData(nbt);
        this.loadData(nbt);
        return oldWerewolve;
    }

    @Override
    public void onLevelChanged(int newLevel, int oldLevel) {
    }

    @Override
    public void onChangedDimension(DimensionType dimensionType, DimensionType dimensionType1) {

    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.actionHandler.deactivateAllActions();
    }

    @Override
    public boolean onEntityAttacked(DamageSource damageSource, float v) {
        return false;
    }

    @Override
    public void onJoinWorld() {
        if (this.getLevel() > 0) {
            this.actionHandler.onActionsReactivated();
        }
    }

    @Override
    public void onPlayerLoggedIn() {

    }

    @Override
    public void onPlayerLoggedOut() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onUpdatePlayer(TickEvent.Phase phase) {

    }


    @Override
    protected void loadUpdate(CompoundNBT nbt) {
        this.actionHandler.readUpdateFromServer(nbt);
        this.skillHandler.readUpdateFromServer(nbt);
        this.killedAnimal = nbt.getInt("bittenAnimal");
        this.packRank = EnumPackRank.by(nbt.getInt("packRank"));
        this.skin = skinOverlays.get(nbt.getInt("skin"));
        this.form = EnumForm.by(nbt.getInt("form"));
    }

    @Override
    protected void writeFullUpdate(CompoundNBT nbt) {
        this.actionHandler.writeUpdateForClient(nbt);
        this.skillHandler.writeUpdateForClient(nbt);
        nbt.putInt("bittenAnimal", this.killedAnimal);
        nbt.putInt("packRank", packRank.ordinal());
        nbt.putInt("skin", skinOverlays.indexOf(skin));
        nbt.putInt("form", form.ordinal());
    }

    private void loadData(CompoundNBT nbt) {
        this.actionHandler.loadFromNbt(nbt);
        this.skillHandler.loadFromNbt(nbt);
        this.killedAnimal = nbt.getInt("bittenEntity");
        this.packRank = EnumPackRank.by(nbt.getInt("packRank"));
        this.skin = skinOverlays.get(nbt.getInt("skin"));
        this.form = EnumForm.by(nbt.getInt("form"));
    }

    private void saveData(CompoundNBT nbt) {
        this.actionHandler.saveToNbt(nbt);
        this.skillHandler.saveToNbt(nbt);
        nbt.putInt("bittenEntity", this.killedAnimal);
        nbt.putInt("packRank", packRank.ordinal());
        nbt.putInt("skin", skinOverlays.indexOf(skin));
        nbt.putInt("form", form.ordinal());
    }


    private static class Storage implements Capability.IStorage<IWerewolfPlayer> {
        @Override
        public void readNBT(Capability<IWerewolfPlayer> capability, IWerewolfPlayer instance, Direction side, INBT nbt) {
            ((WerewolfPlayer) instance).loadData((CompoundNBT) nbt);
        }

        @Override
        public INBT writeNBT(Capability<IWerewolfPlayer> capability, IWerewolfPlayer instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            ((WerewolfPlayer) instance).saveData(nbt);
            return nbt;
        }
    }

}
