package io.github.lukas2005.supernaturalcreatures.skill.vampire;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SkillSunScreen extends Skill {

	public static final float BASE_SUN_DAMAGE = 6f;

	int lvl;

	public SkillSunScreen(int lvl) {
		super("sunScreenLvl"+lvl);
		setCost(lvl+2*lvl);
		this.lvl = lvl;
	}

	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent e, IPlayerDataCapability playerData) {
		super.onPlayerTick(e, playerData);
		playerData.setData("vampire.sunDamage", String.valueOf(BASE_SUN_DAMAGE-2*lvl));
	}
}
