package io.github.lukas2005.supernaturalcreatures.skill;

import java.util.ArrayList;
import java.util.List;

public class SkillTree {

	List<Level> levels = new ArrayList<>();

	public Level addLevel() {
		Level l = new Level();
		levels.add(l);
		l.tree = this;
		return l;
	}

	public List<Level> getLevels() {
		return levels;
	}

}
