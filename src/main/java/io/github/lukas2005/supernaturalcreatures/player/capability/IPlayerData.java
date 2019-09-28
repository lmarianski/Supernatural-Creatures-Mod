package io.github.lukas2005.supernaturalcreatures.player.capability;

import io.github.lukas2005.supernaturalcreatures.network.CapabilitySyncMessage;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import io.github.lukas2005.supernaturalcreatures.player.ICreatureData;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;


public interface IPlayerData {

	void setCreatureType(CreatureType type);
	CreatureType getCreatureType();

	void setConversion(CreatureType type);
	CreatureType getConversion();

	void setConvertStartTime(long convertStartTime);
	long getConvertStartTime();

	void setTransformed(boolean transformed);
	boolean isTransformed();

	void setLevel(int newLevel);
	int getLevel();

	ICreatureData getCreatureData(CreatureType type);
	void addCreatureData(CreatureType type, ICreatureData data);
	Map<CreatureType, ICreatureData> getDataMap();

	void cloneTo(IPlayerData target);
	void syncData(boolean all);

//	List<Skill> getSkills();
//	void setSkills(List<Skill> skills);
//	void addSkill(Skill skills);
//	void removeSkill(Skill skills);
//
//	int getSkillPoints();
//	void setSkillPoints(int amount);
//
//	int getUsedSkillPoints();
//	void setUsedSkillPoints(int amount);
//
//	List<Skill> getHighestLevelSkills(boolean reCreate);

	class Storage implements Capability.IStorage<IPlayerData> {

		@Nullable
		@Override
		public INBT writeNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();

			nbt.putInt("creatureType", instance.getCreatureType().ordinal());
			nbt.putInt("conversion", instance.getConversion() == null ? -1 : instance.getConversion().ordinal());
			nbt.putLong("conversionStart", instance.getConvertStartTime());

			nbt.putBoolean("transformed", instance.isTransformed());

			CompoundNBT dataMap = new CompoundNBT();
			for (Map.Entry<CreatureType, ICreatureData> e : instance.getDataMap().entrySet()) {
				dataMap.put(e.getKey().name(), e.getValue().toNBT());
			}
			nbt.put("dataMap", dataMap);

			nbt.putInt("level", instance.getLevel());

//			ListNBT skillList = new ListNBT();
//
//			for (Skill e : instance.getSkills()) {
//				if (e != null) {
//					skillList.add(new StringNBT(e.toString()));
//				}
//			}
//
//			nbt.put("skillList", skillList);

//			nbt.putInt("skillPoints", instance.getSkillPoints());
//			nbt.putInt("usedSkillPoints", instance.getUsedSkillPoints());


			return nbt;
		}

		@Override
		public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side, INBT nbtBase) {
			CompoundNBT nbt = (CompoundNBT) nbtBase;

			instance.setCreatureType(CreatureType.byOrdinal(nbt.getInt("creatureType")));
			instance.setConversion(nbt.getInt("conversion") == -1 ? null : CreatureType.byOrdinal(nbt.getInt("conversion")));
			instance.setConvertStartTime(nbt.getLong("conversionStart"));

			instance.setTransformed(nbt.getBoolean("transformed"));

			CompoundNBT dataMap = nbt.getCompound("dataMap");
            Map<CreatureType, ICreatureData> map = instance.getDataMap();
			for (String key : dataMap.keySet()) {
			    CreatureType type = CreatureType.valueOf(key);
			    ICreatureData data = map.get(type);
				if (data != null) {
                    data.fromNBT(dataMap.get(key));
                } else {
				    data = type.getBehaviour().creatureDataFromNBT(dataMap.get(key));
				    map.put(type, data);
                }
			}

			instance.setLevel(nbt.getInt("level"));

//			ListNBT skillList = (ListNBT) nbt.get("skillList");
//
//			for (INBT base : skillList) {
//				if (base.getId() != 0) {
//					instance.addSkill(Skill.skills.get(((StringNBT) base).getString()));
//				}
//			}
//
//			instance.setUsedSkillPoints(nbt.getInt("usedSkillPoints"));
//			instance.setSkillPoints(nbt.getInt("skillPoints"));
		}

	}

	class Impl implements IPlayerData {

		@Override
		public void setCreatureType(CreatureType type) {
		}

		@Override
		public CreatureType getCreatureType() {
			return null;
		}

		@Override
		public void setConversion(CreatureType type) {
		}

		@Override
		public CreatureType getConversion() {
			return null;
		}

		@Override
		public void setConvertStartTime(long convertTime) {
		}

		@Override
		public long getConvertStartTime() {
			return 0;
		}

		@Override
		public void setTransformed(boolean transformed) {
		}

		@Override
		public boolean isTransformed() {
			return false;
		}

		@Override
		public void setLevel(int newLevel) {
		}

		@Override
		public int getLevel() {
			return 0;
		}

		@Override
		public ICreatureData getCreatureData(CreatureType type) {
			return null;
		}

		@Override
		public void addCreatureData(CreatureType type, ICreatureData data) {
		}

        @Override
        public Map<CreatureType, ICreatureData> getDataMap() {
            return null;
        }

        @Override
		public void cloneTo(IPlayerData target) {
		}

		@Override
		public void syncData(boolean all) {
		}

//		@Override
//		public List<Skill> getSkills() {
//			return skills;
//		}
//
//		@Override
//		public void setSkills(List<Skill> skills) {
//			setSkillPoints(0);
//			setUsedSkillPoints(0);
//
//			this.skills = new LinkedList<>();
//			for (Skill skill : skills) {
//				addSkill(skill);
//			}
//		}
//
//		@Override
//		public void addSkill(Skill skill) {
//			if (getSkillPoints() >= skill.getCost()) {
//				if (!skills.contains(skill)) {
//					if (skill.getDependencies().size() == 0 || getSkills().containsAll(skill.getDependencies())) {
//						skills.add(skill);
//					}
//				}
//
//				setSkillPoints(getSkillPoints()-skill.getCost());
//				setUsedSkillPoints(getUsedSkillPoints()+skill.getCost());
//			}
//		}
//
//		@Override
//		public void removeSkill(Skill skill) {
//			if (skills.contains(skill)) {
//				if (!Utils.containsAny(getSkills(), skill.getDependants())) {
//					skills.remove(skill);
//					setSkillPoints(getSkillPoints()+skill.getCost());
//					setUsedSkillPoints(getUsedSkillPoints()-skill.getCost());
//				}
//			}
//		}
//
//		@Override
//		public int getSkillPoints() {
//			return skillPoints;
//		}
//
//		@Override
//		public void setSkillPoints(int amount) {
//			skillPoints = amount;
//		}
//
//		@Override
//		public int getUsedSkillPoints() {
//			return usedSkillPoints;
//		}
//
//		@Override
//		public void setUsedSkillPoints(int amount) {
//			usedSkillPoints = amount;
//		}
//
//		@Override
//		public List<Skill> getHighestLevelSkills(boolean reCreate) {
//			if (reCreate) hLSkills = Utils.getHighestLevelSkills(getSkills());
//			return hLSkills;
//		}
	}

}
