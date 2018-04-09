package io.github.lukas2005.supernaturalcreatures.capabilities;

import io.github.lukas2005.supernaturalcreatures.NBTTagListIterator;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import io.github.lukas2005.supernaturalcreatures.network.CapabilitySyncMessage;
import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public interface IPlayerDataCapability {

	void setCreatureType(CreatureType type);
	CreatureType getCreatureType();

	void setTransformed(boolean transformed);
	boolean isTransformed();

	void setLevel(int newLevel);
	void incrementLevel();
	void decrementLevel();
	int getLevel();

	void setData(String key, String value);
	String getData(String key);

	Map<String, String> getDataMap();

	void cloneTo(IPlayerDataCapability target);

	void syncData(EntityPlayer player);

	ArrayList<Skill> getSkills();
	void setSkills(ArrayList<Skill> skills);
	void addSkill(Skill skills);
	void removeSkill(Skill skills);

	class Storage implements Capability.IStorage<IPlayerDataCapability> {

		@Override
		public NBTBase writeNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();

			nbt.setInteger("creatureType", instance.getCreatureType().ordinal());
			nbt.setBoolean("transformed", instance.isTransformed());

			NBTTagCompound dataMap = new NBTTagCompound();

			for (Map.Entry<String, String> e : instance.getDataMap().entrySet()) {
				dataMap.setString(e.getKey(), e.getValue());
			}

			nbt.setTag("dataMap", dataMap);

			nbt.setInteger("level", instance.getLevel());

			NBTTagList skillList = new NBTTagList();

			for (Skill e : instance.getSkills()) {
				skillList.appendTag(new NBTTagString(e.toString()));
			}

			nbt.setTag("skillList", skillList);

			return nbt;
		}

		@Override
		public void readNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, EnumFacing side, NBTBase nbtBase) {
			NBTTagCompound nbt = (NBTTagCompound) nbtBase;

			instance.setCreatureType(CreatureType.byOrdinal(nbt.getInteger("creatureType")));
			instance.setTransformed(nbt.getBoolean("transformed"));

			NBTTagCompound dataMap = nbt.getCompoundTag("dataMap");

			for (String key : dataMap.getKeySet()) {
				instance.setData(key, dataMap.getString(key));
			}

			instance.setLevel(nbt.getInteger("level"));

			NBTTagList skillList = nbt.getTagList("skillList", 8);

			for (NBTBase base : new NBTTagListIterator(skillList)) {
				instance.addSkill(Skill.skills.get(new ResourceLocation(base.toString())));
			}
		}
	}

	class Impl implements IPlayerDataCapability {

		CreatureType creatureType = CreatureType.HUMAN;
		boolean transformed = false;

		Map<String, String> data = new HashMap<>();

		int level = 0;

		ArrayList<Skill> skills = new ArrayList<>();

		@Override
		public void setCreatureType(CreatureType type) {
			creatureType = type;
		}

		@Override
		public CreatureType getCreatureType() {
			return creatureType;
		}

		@Override
		public void setTransformed(boolean transformed) {
			this.transformed = transformed;
		}

		@Override
		public boolean isTransformed() {
			return transformed;
		}

		@Override
		public void setLevel(int newLevel) {
			level = newLevel;
		}

		@Override
		public void incrementLevel() {
			level++;
		}

		@Override
		public void decrementLevel() {
			level--;
		}

		@Override
		public int getLevel() {
			return level;
		}

		@Override
		public void setData(String key, String value) {
			if (!data.containsKey(key)) {
				data.put(key, value);
			} else {
				data.remove(key);
				setData(key, value);
			}
		}

		@Override
		public String getData(String key) {
			return data.get(key);
		}

		@Override
		public Map<String, String> getDataMap() {
			return Collections.unmodifiableMap(data);
		}

		@Override
		public void cloneTo(IPlayerDataCapability target) {
			NBTTagCompound nbt = (NBTTagCompound) ModCapabilities.PLAYER_DATA_CAPABILITY.writeNBT(this, null);
			ModCapabilities.PLAYER_DATA_CAPABILITY.readNBT(target, null, nbt);
		}

		@Override
		public void syncData(EntityPlayer player) {
			if (!player.getEntityWorld().isRemote) {
				NetworkManager.INSTANCE.sendTo(new CapabilitySyncMessage<>(this, ModCapabilities.PLAYER_DATA_CAPABILITY), (EntityPlayerMP) player);
			}
		}

		@Override
		public ArrayList<Skill> getSkills() {
			return skills;
		}

		@Override
		public void setSkills(ArrayList<Skill> skills) {
			this.skills = skills;
		}

		@Override
		public void addSkill(Skill skill) {
			if (!skills.contains(skill)) {
				skills.add(skill);
			}
		}

		@Override
		public void removeSkill(Skill skill) {
			skills.remove(skill);
		}
	}

	class Provider implements ICapabilitySerializable<NBTTagCompound> {

		Impl impl;

		public Provider() {
			impl = new Impl();
		}

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == ModCapabilities.PLAYER_DATA_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
			return (T) impl;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) ModCapabilities.PLAYER_DATA_CAPABILITY.writeNBT(impl, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			ModCapabilities.PLAYER_DATA_CAPABILITY.readNBT(impl, null, nbt);
		}
	}

	static void register() {
		CapabilityManager.INSTANCE.register(IPlayerDataCapability.class, new Storage(), Impl.class);
	}

}
