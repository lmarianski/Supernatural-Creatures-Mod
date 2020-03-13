package io.github.lukas2005.supernaturalcreatures.skill;

import io.github.lukas2005.supernaturalcreatures.skill.tree.SkillTree;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SkillHelper {

	public static List<Skill> createSkillChain(Class<? extends Skill> clazz, boolean chainSkillsTogether, SkillTree tree, BiConsumer<Object[], Skill> consumer, Object...constructorParams) throws Exception {
		Skill prevSkill = null;
		List<Skill> skills = new ArrayList<>();
		for (Level l : tree.levels) {

			Constructor c = clazz.getConstructors()[0];
			c.setAccessible(true);

			Skill s = (Skill) c.newInstance(constructorParams);
			l.addSkill(s);

			if (chainSkillsTogether && prevSkill != null) {
				s.setRequiredSkills(prevSkill);
			}

			consumer.accept(constructorParams, s);

			skills.add(s);
			prevSkill = s;
		}
		return skills;
	}

}
