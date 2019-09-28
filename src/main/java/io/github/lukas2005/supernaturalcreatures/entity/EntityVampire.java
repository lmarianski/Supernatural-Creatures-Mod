package io.github.lukas2005.supernaturalcreatures.entity;

import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class EntityVampire extends CreatureEntity implements ICreature {

	protected EntityVampire(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static <T extends MobEntity> boolean spawnPlacement(EntityType<T> entityType, IWorld world, SpawnReason spawnReason, BlockPos blockPos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && ModEntities.spawnPredicateLight(world, blockPos, random) && ModEntities.spawnPredicateCanSpawn(entityType, world, spawnReason, blockPos, random);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		//this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(1.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 0.9));
		this.goalSelector.addGoal(9, new RandomWalkingGoal(this, 0.7D));
		this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 20.0F, 0.6F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, VillagerEntity.class, 8.0F));
		//this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {

		// TODO: Custom bite logic

		return super.attackEntityAsMob(entityIn);
	}

//	@Override
//	public boolean getCanSpawnHere() {
//		return !worldObj.isDaytime() && super.getCanSpawnHere();
//	}


	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
	}

	@Override
	public void livingTick() {
		setAttackTarget(world.getClosestPlayer(this, 10));
		if (this.world.isDaytime() && !this.world.isRemote) {
			float f = this.getBrightness();
			BlockPos blockpos = this.getRidingEntity() instanceof BoatEntity ? (new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ);

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canBlockSeeSky(blockpos)) {
				boolean flag = true;
				ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.HEAD);

				if (itemstack.isDamageable()) {
					if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
						//this.renderBrokenItemStack(itemstack);
						this.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
					}
				}

				flag = false;

				if (flag) {
					this.setFire(8);
				}
			}
		}
		super.livingTick();
	}

	@Override
	public CreatureType getCreatureType() {
		return CreatureType.VAMPIRE;
	}

}
