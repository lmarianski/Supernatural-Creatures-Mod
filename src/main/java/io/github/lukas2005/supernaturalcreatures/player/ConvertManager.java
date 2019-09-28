package io.github.lukas2005.supernaturalcreatures.player;

import io.github.lukas2005.supernaturalcreatures.player.behaviour.ICreatureBehaviour;
import io.github.lukas2005.supernaturalcreatures.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class ConvertManager {

    public static final Map<Map.Entry<CreatureType, Class<? extends LivingEntity>>, IConvertFactory<Entity>> CONVERT_FACTORIES = new HashMap<>();

    static {
        CONVERT_FACTORIES.put(new AbstractMap.SimpleEntry<>(CreatureType.VAMPIRE, SheepEntity.class), (entity) -> {
            Entity e = ModEntities.VAMPIRE.create(entity.world);
            e.copyLocationAndAnglesFrom(entity);

            return e;
        });
    }

    public static boolean canBecome(LivingEntity entity, CreatureType creature) {
        if (entity instanceof PlayerEntity) {
            return SCMPlayer.of((PlayerEntity) entity).canBecome(creature);
        } else {
            Map.Entry<CreatureType, Class<? extends LivingEntity>> entry = new HashMap.SimpleEntry<>(creature, entity.getClass());

            return CONVERT_FACTORIES.containsKey(entry);
        }
    }

    public static void convertEntity(LivingEntity entity, CreatureType type, boolean instant) {
        if (entity instanceof PlayerEntity) {
            SCMPlayer.of((PlayerEntity) entity).convert(type, instant);
        } else {
            Map.Entry<CreatureType, Class<? extends LivingEntity>> entry = new HashMap.SimpleEntry<>(type, entity.getClass());

            if (CONVERT_FACTORIES.containsKey(entry)) {
                IConvertFactory<Entity> factory = CONVERT_FACTORIES.get(entry);

                entity.setHealth(0);
                Entity newEntity = factory.convertEntity(entity);
                entity.getEntityWorld().addEntity(newEntity);
            }
        }
    }

    /**
     * Ends the infection, either converting the entity or not
     * @param entity The entity
     * @param convert Whether or not the entity should get converted
     */
    public static void endInfection(LivingEntity entity, boolean convert) {
        if (entity instanceof PlayerEntity) {
            SCMPlayer.of((PlayerEntity) entity).endInfection(convert);
        }
    }

    public static void addRandom(LivingEntity entity, CreatureType type) {
        if (entity.getRNG().nextFloat() >= 0.6) {
            convertEntity(entity, type, false);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            if (e.player.isAlive()) {
                SCMPlayer playerData = SCMPlayer.of(e.player);

                if (playerData.getConversion() != null) {
                    ICreatureBehaviour behaviour = playerData.getConversion().getBehaviour();
                    behaviour.onInfectedTick(playerData, e.player.world.getGameTime() - playerData.getConvertStartTime());
                }
            }
        }
    }

    public interface IConvertFactory<T extends Entity> {

        T convertEntity(LivingEntity entity);

    }

}
