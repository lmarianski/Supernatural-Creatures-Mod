package io.github.lukas2005.supernaturalcreatures;

import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import io.github.lukas2005.supernaturalcreatures.api.entity.werewolf.IWerewolfPlayer;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class Reference {
    public static final String MOD_ID = "scm";

    public final static IAttribute biteDamage   = new RangedAttribute(null, MOD_ID+".bite_damage"            , 2D, 0D, 100D);
    public final static IAttribute harvestSpeed = new RangedAttribute(null, MOD_ID+"werewolves.harvest_speed", 1D, 0D, 100D);
    public final static IAttribute harvestLevel = new RangedAttribute(null, MOD_ID+"werewolves.harvest_level", 0D, 0D, 3D);
}
