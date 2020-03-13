package io.github.lukas2005.supernaturalcreatures.entity;

import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.entity.vampire.BasicVampireEntity;
import io.github.lukas2005.supernaturalcreatures.ModFactions;
import io.github.lukas2005.supernaturalcreatures.api.entity.werewolf.IWerewolfMob;
import io.github.lukas2005.supernaturalcreatures.enums.EnumPackRank;
import io.github.lukas2005.supernaturalcreatures.world.Pack;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class EntityWerewolf extends MonsterEntity implements IWerewolfMob {

    public static final DataParameter<Boolean> IS_TRANSFORMED = EntityDataManager.createKey(EntityWerewolf.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Integer> PACK_RANK = EntityDataManager.createKey(EntityWerewolf.class, DataSerializers.VARINT);

    protected EntityWerewolf(EntityType<? extends MonsterEntity> type, World worldIn) {
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
        this.dataManager.register(IS_TRANSFORMED, true);
        this.dataManager.register(PACK_RANK, EnumPackRank.OMEGA.ordinal());
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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(EntityWerewolf.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EntityWerewolf.class, false));
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
    public EnumPackRank getPackRank() {
        return EnumPackRank.OMEGA;
    }

    @Override
    public boolean isTransformed() {
        return true;
    }
}
