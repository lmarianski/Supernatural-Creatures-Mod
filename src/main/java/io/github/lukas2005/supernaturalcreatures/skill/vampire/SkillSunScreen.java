package io.github.lukas2005.supernaturalcreatures.skill.vampire;

import io.github.lukas2005.supernaturalcreatures.player.capability.IPlayerData;
import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import net.minecraftforge.event.TickEvent;

public class SkillSunScreen extends Skill {

	public static final float BASE_SUN_DAMAGE = 6f;

	int lvl;

	public SkillSunScreen(int lvl) {
		super("sunScreenLvl"+lvl);
		setCost(lvl+2*lvl);
		this.lvl = lvl;
	}

	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent e, IPlayerData playerData) {
		super.onPlayerTick(e, playerData);
		//playerData.setData("vampire.sunDamage", String.valueOf(BASE_SUN_DAMAGE-2*lvl));
	}
}
