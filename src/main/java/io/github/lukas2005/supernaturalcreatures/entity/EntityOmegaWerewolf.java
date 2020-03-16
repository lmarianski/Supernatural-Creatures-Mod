package io.github.lukas2005.supernaturalcreatures.entity;

import de.teamlapen.vampirism.api.entity.actions.IActionHandlerEntity;
import de.teamlapen.vampirism.api.entity.actions.IEntityActionUser;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.world.IVillageAttributes;
import de.teamlapen.vampirism.entity.vampire.BasicVampireEntity;
import io.github.lukas2005.supernaturalcreatures.ModFactions;
import io.github.lukas2005.supernaturalcreatures.api.entity.werewolf.IBetaWerewolf;
import io.github.lukas2005.supernaturalcreatures.api.entity.werewolf.IOmegaWerewolf;
import io.github.lukas2005.supernaturalcreatures.enums.EnumForm;
import io.github.lukas2005.supernaturalcreatures.world.Pack;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class EntityOmegaWerewolf extends MonsterEntity implements IOmegaWerewolf, IEntityActionUser {

    protected EntityOmegaWerewolf(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static <T extends MobEntity> boolean spawnPlacement(EntityType<T> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && ModEntities.spawnPredicateLight(world, blockPos, random) && ModEntities.spawnPredicateCanSpawn(entityType, world, spawnReason, blockPos, random);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {

        if (entityIn instanceof LivingEntity) {
            //ConvertManager.addRandom((LivingEntity) entityIn, CreatureType.WEREWOLF);
        }

        return super.attackEntityAsMob(entityIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(4, new ZombieEntity.AttackTurtleEggGoal(this, 1.0D, 3));
        this.goalSelector.addGoal(2, new BreatheAirGoal(this));
        this.goalSelector.addGoal(2, new SwimGoal(this));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, ()->true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(EntityOmegaWerewolf.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityOmegaWerewolf.class, 10, false, false, (e) -> ((EntityOmegaWerewolf)e).getPack() != getPack()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, BasicVampireEntity.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public IFaction getFaction() {
        return ModFactions.WEREWOLF_FACTION;
    }

    @Override
    public LivingEntity getRepresentingEntity() {
        return this;
    }

    @Override
    public Pack getPack() {
        return null;
    }

    @Override
    public void setForm(EnumForm form) {}

    @Override
    public IActionHandlerEntity getActionHandler() {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void setLevel(int i) {

    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int suggestLevel(de.teamlapen.vampirism.api.difficulty.Difficulty difficulty) {
        return 0;
    }

    @Override
    public void attackVillage(IVillageAttributes iVillageAttributes) {

    }

    @Override
    public void defendVillage(IVillageAttributes iVillageAttributes) {

    }

    @Nullable
    @Override
    public AxisAlignedBB getTargetVillageArea() {
        return null;
    }

    @Override
    public boolean isAttackingVillage() {
        return false;
    }

    @Override
    public boolean isDefendingVillage() {
        return false;
    }

    @Override
    public void stopVillageAttackDefense() {

    }

    @Nullable
    @Override
    public IVillageAttributes getVillageAttributes() {
        return null;
    }

    private IBetaWerewolf leader = null;

    @Override
    @Nullable
    public IBetaWerewolf getLeader() {
        return leader;
    }

    @Override
    public void setLeader(IBetaWerewolf newLeader) {
        if (leader != null)
            this.leader.removeFollower(this);

        this.leader = newLeader;

        if (leader != null)
            this.leader.addFollower(this);
    }
}
