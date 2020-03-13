//package io.github.lukas2005.supernaturalcreatures.player;
//
//import io.github.lukas2005.supernaturalcreatures.AttributeModifier;
//import io.github.lukas2005.supernaturalcreatures.Main;
//import io.github.lukas2005.supernaturalcreatures.network.CapabilitySyncMessage;
//import io.github.lukas2005.supernaturalcreatures.network.ISyncable;
//import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
//import io.github.lukas2005.supernaturalcreatures.player.capability.IPlayerData;
//import io.github.lukas2005.supernaturalcreatures.ModCapabilities;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.common.capabilities.*;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.fml.network.PacketDistributor;
//
//import javax.annotation.Nonnull;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SCMPlayer implements IPlayerData, ISyncable {
//
//    @CapabilityInject(IPlayerData.class)
//    public static final Capability<IPlayerData> CAP = null;
//    public static final ResourceLocation CAP_KEY = new ResourceLocation(Main.MOD_ID, "player_data");
//
//    public PlayerEntity entity;
//
//    CreatureType creatureType = CreatureType.HUMAN;
//    CreatureType conversion = null;
//    long convertTime = 0L;
//
//    boolean transformed = false;
//
//    Map<CreatureType, ICreatureData> creatureData = new HashMap<>();
//
//    int level = 0;
//
//    public SCMPlayer(PlayerEntity entity) {
//        this.entity = entity;
//    }
//
//    public static SCMPlayer of(PlayerEntity entity) {
//        return (SCMPlayer) entity.getCapability(CAP, null).orElseThrow(()->new IllegalStateException("Entity doesn't have capability!"));
//    }
//
//    public static void registerCapability() {
//        CapabilityManager.INSTANCE.register(IPlayerData.class, new Storage(), IPlayerData.Impl::new);
//        ModCapabilities.CAPABILITY_MAP.put(CAP_KEY, CAP);
//    }
//
//    public static ICapabilityProvider createNewCapability(final PlayerEntity player) {
//        return new ICapabilitySerializable<CompoundNBT>() {
//
//            final IPlayerData inst = new SCMPlayer(player);
//            final LazyOptional<IPlayerData> opt = LazyOptional.of(() -> inst);
//
//            @Override
//            public void deserializeNBT(CompoundNBT nbt) {
//                CAP.getStorage().readNBT(CAP, inst, null, nbt);
//            }
//
//            @Nonnull
//            @Override
//            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
//                return CAP.orEmpty(capability, opt);
//            }
//
//            @Override
//            public CompoundNBT serializeNBT() {
//                return (CompoundNBT) CAP.getStorage().writeNBT(CAP, inst, null);
//            }
//        };
//    }
//
//    public boolean canBecome(CreatureType creature) {
//        return getConversion() == null;
//    }
//
//    public void convert(CreatureType type, boolean instant) {
//        if (canBecome(type)) {
//            setConversion(type);
//            setConvertStartTime(entity.world.getGameTime());
//            syncData(true);
//
//            getCreatureType().getBehaviour().onInfected(this);
//
//            if (instant) {
//                endInfection(true);
//            }
//        }
//    }
//
//    public void endInfection(boolean convert) {
//        if (getConversion() != null) {
//            if (convert) {
//                setCreatureType(getConversion());
//                setConvertStartTime(0);
//                setConversion(null);
//
//                setLevel(1);
//
//                getCreatureType().getBehaviour().onInfectionEnd(this, true);
//            } else {
//                setConvertStartTime(0);
//                setConversion(null);
//
//                getCreatureType().getBehaviour().onInfectionEnd(this, false);
//            }
//            syncData(true);
//        }
//    }
//
//
//
//    @Override
//    public void setCreatureType(CreatureType type) {
//        creatureType = type;
//    }
//
//    @Override
//    public CreatureType getCreatureType() {
//        return creatureType;
//    }
//
//    @Override
//    public void setConversion(CreatureType type) {
//        conversion = type;
//    }
//
//    @Override
//    public CreatureType getConversion() {
//        return conversion;
//    }
//
//    @Override
//    public void setConvertStartTime(long convertTime) {
//        this.convertTime = convertTime;
//    }
//
//    @Override
//    public long getConvertStartTime() {
//        return convertTime;
//    }
//
//    @Override
//    public void setTransformed(boolean transformed) {
//        this.transformed = transformed;
//    }
//
//    @Override
//    public boolean isTransformed() {
//        return transformed;
//    }
//
//    @Override
//    public void setLevel(int newLevel) {
//        if (newLevel >= 1) {
//            level = newLevel;
//
//            //skillPoints = (level / 2) - usedSkillPoints;
//        } else {
//            level = 1;
//        }
//
//        if (getCreatureType() == CreatureType.HUMAN) {
//            level = 0;
//        }
//
//        AttributeModifier.applyModifiers(entity);
//    }
//
//    @Override
//    public int getLevel() {
//        return level;
//    }
//
//    @Override
//    public ICreatureData getCreatureData(CreatureType type) {
//        return creatureData.get(type);
//    }
//
//    @Override
//    public void addCreatureData(CreatureType type, ICreatureData data) {
//        creatureData.put(type, data);
//    }
//
//    @Override
//    public Map<CreatureType, ICreatureData> getDataMap() {
//        return creatureData;
//    }
//
//    @Override
//    public void cloneTo(IPlayerData target) {
//        CompoundNBT nbt = (CompoundNBT) CAP.writeNBT(this, null);
//        CAP.readNBT(target, null, nbt);
//    }
//
//    @Override
//    public void syncData(boolean all) {
//        if (!entity.getEntityWorld().isRemote) {
//            if (all) {
//                NetworkManager.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new CapabilitySyncMessage(this, CAP, CapabilitySyncMessage.SyncTarget.ENTITY));
//                NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new CapabilitySyncMessage(this, CAP, CapabilitySyncMessage.SyncTarget.PLAYER));
//            } else {
//                NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new CapabilitySyncMessage(this, CAP, CapabilitySyncMessage.SyncTarget.PLAYER));
//            }
//        }
//    }
//
//    @Override
//    public ResourceLocation getCapKey() {
//        return CAP_KEY;
//    }
//
//    @Override
//    public int getEntityID() {
//        return entity.getEntityId();
//    }
//}
