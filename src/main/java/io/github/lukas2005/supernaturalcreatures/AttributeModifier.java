package io.github.lukas2005.supernaturalcreatures;

import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.ICreatureBehaviour;
import io.github.lukas2005.supernaturalcreatures.enums.BuffType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttributeModifier {

    private static Map<IAttribute, UUID> modifiers = new HashMap<>();

    static {
        modifiers.put(SharedMonsterAttributes.MAX_HEALTH          , UUID.fromString("2ca53712-a075-40ca-b088-49f31ea4d7cb"));
        modifiers.put(SharedMonsterAttributes.MOVEMENT_SPEED      , UUID.fromString("24e7b06c-5f6b-41a2-8436-3c5314419684"));
        modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE       , UUID.fromString("93c8f098-df6c-4c2b-b27d-0384bb1fe55d"));
        //modifiers.put(SharedMonsterAttributes.ATTACK_KNOCKBACK    , UUID.fromString("ab8c6940-4505-4d70-9539-7ed534832169b"));
        modifiers.put(SharedMonsterAttributes.ATTACK_SPEED        , UUID.fromString("5b9ac7dd-c95d-47ef-8acd-56c3c6f9d9f1"));
        modifiers.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, UUID.fromString("2d3edc3f-1820-4b80-b001-33bb844fad86"));
    }

    public static void applyModifiers(PlayerEntity player) {
        SCMPlayer playerData = SCMPlayer.of(player);
        ICreatureBehaviour creature = playerData.getCreatureType().getBehaviour();

        for (Map.Entry<IAttribute, UUID> modifier : modifiers.entrySet()) {
            IAttributeInstance instance = player.getAttribute(modifier.getKey());

            rmMod(instance, modifier.getValue());

            double mod = creature.getBuff(BuffType.byAttribute(modifier.getKey()), playerData);
            net.minecraft.entity.ai.attributes.AttributeModifier.Operation op = creature.getOperation(BuffType.byAttribute(modifier.getKey()), playerData);

            instance.applyModifier(new net.minecraft.entity.ai.attributes.AttributeModifier(modifier.getValue(), modifier.getKey().getName() + " Boost", mod, op));
        }
    }

    /**
     * Calculates the modifier effect. You can decide how the modifier changes with higher levels, by using different types. Suggested values are 1/2 for a square root like behavior or 1 for a linear
     * change
     *
     * @param level  level
     * @param lcap   Level the modifier does not get any stronger
     * @param maxMod Maximal modifier effect
     * @param type   modifier type
     * @return value between 0 and maxMod
     */
    public static double calculateModifierValue(int level, int lcap, double maxMod, double type) {
        return Math.pow((level > lcap ? lcap : level), type) / Math.pow(lcap, type) * maxMod;
    }

    /**
     * Removes existing modifiers
     *
     * @param att  Attribute
     * @param uuid UUID of modifier to remove
     */
    private static void rmMod(IAttributeInstance att, UUID uuid) {
        //if (att != null) {
            net.minecraft.entity.ai.attributes.AttributeModifier m = att.getModifier(uuid);
            if (m != null) {
                att.removeModifier(m);
            }
        //}
    }

    /**
     *
     * @param attribute the attribute whose UUID you need
     * @return the UUID of the given attribute
     */
    public static UUID getUUID(IAttribute attribute) {
        return modifiers.get(attribute);
    }
}
