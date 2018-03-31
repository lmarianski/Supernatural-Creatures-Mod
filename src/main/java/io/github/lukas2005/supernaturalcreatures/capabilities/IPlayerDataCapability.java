package io.github.lukas2005.supernaturalcreatures.capabilities;

import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import io.github.lukas2005.supernaturalcreatures.network.PlayerDataMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;


public interface IPlayerDataCapability {

	void setCreatureType(CreatureType type);
	CreatureType getCreatureType();

	class Storage implements Capability.IStorage<IPlayerDataCapability> {

		@Override
		public NBTBase writeNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();

			nbt.setInteger("creatureType", instance.getCreatureType().ordinal());

			return nbt;
		}

		@Override
		public void readNBT(Capability<IPlayerDataCapability> capability, IPlayerDataCapability instance, EnumFacing side, NBTBase nbt) {
			instance.setCreatureType(CreatureType.byOrdinal(((NBTTagCompound) nbt).getInteger("creatureType")));
		}
	}

	class Impl implements IPlayerDataCapability {

		EntityPlayer player;

		CreatureType creatureType = CreatureType.HUMAN;

		public Impl(EntityPlayer p) {
			player = p;
		}

		@Override
		public void setCreatureType(CreatureType type) {
			creatureType = type;

			if (!player.getEntityWorld().isRemote) {
				NetworkManager.INSTANCE.sendTo(new PlayerDataMessage(this), (EntityPlayerMP) player);
			}
		}

		@Override
		public CreatureType getCreatureType() {
			return creatureType;
		}
	}

	class Provider implements ICapabilitySerializable<NBTTagCompound> {

		Impl impl;

		public Provider(EntityPlayer p) {
			impl = new Impl(p);
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
			return (NBTTagCompound) ModCapabilities.PLAYER_DATA_CAPABILITY.getStorage().writeNBT(ModCapabilities.PLAYER_DATA_CAPABILITY, impl, null);
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
