package io.github.lukas2005.supernaturalcreatures.skill;

import java.util.ArrayList;
import java.util.List;

public class Level {

	public List<Skill> skills = new ArrayList<>();

	protected SkillTree tree;

	public Skill addSkill(Skill skill) {
		skills.add(skill);
		skill.level = this;
		skill.tree = tree;
		return skill;
	}

}
