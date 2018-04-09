package io.github.lukas2005.supernaturalcreatures.skill.vampire;

import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SkillSunScreen extends Skill {
	public SkillSunScreen(int lvl) {
		super("sunScreenLvl"+lvl);
		setCost(lvl+2*lvl);
	}

	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		super.onPlayerTick(e);
	}
}
