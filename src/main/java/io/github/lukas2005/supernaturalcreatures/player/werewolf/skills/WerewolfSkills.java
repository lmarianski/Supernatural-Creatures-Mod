package io.github.lukas2005.supernaturalcreatures.player.werewolf.skills;

import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.core.ModRegistries;
import de.teamlapen.vampirism.player.skills.VampirismSkill;
import io.github.lukas2005.supernaturalcreatures.ModFactions;
import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.api.entity.werewolf.IWerewolfPlayer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class WerewolfSkills {

    public static final DeferredRegister<ISkill> SKILLS = new DeferredRegister<>(ModRegistries.SKILLS, Reference.MOD_ID);

    public static final RegistryObject<ISkill> WEREWOLF = SKILLS.register(
            "werewolf",
            () -> new SimpleWerewolfSkill(false)
    );

//    @SubscribeEvent
//    public void onSkillNodeCreated(SkillEvent.CreatedNode event) {
//        if (event.getNode().isRoot()) {
//            if (event.getNode().getFaction().equals(WReference.WEREWOLF_FACTION)) {
//                WerewolfSkills.buildSkillTree(event.getNode());
//            }
//        }
//    }

    public static class SimpleWerewolfSkill extends VampirismSkill<IWerewolfPlayer> {
        public SimpleWerewolfSkill(boolean desc) {
            super(ModFactions.WEREWOLF_FACTION);
            if (desc) {
                this.setHasDefaultDescription();
            }
        }
    }

}
