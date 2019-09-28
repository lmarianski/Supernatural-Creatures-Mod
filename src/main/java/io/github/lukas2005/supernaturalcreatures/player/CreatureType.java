package io.github.lukas2005.supernaturalcreatures.player;

import io.github.lukas2005.supernaturalcreatures.player.behaviour.HumanBehaviour;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.ICreatureBehaviour;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.VampireBehaviour;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.WerewolfBehaviour;

public enum CreatureType {

	HUMAN,
	VAMPIRE(new VampireBehaviour()),
	WEREWOLF(new WerewolfBehaviour());

	private ICreatureBehaviour behaviour;

	CreatureType() {
		behaviour = new HumanBehaviour();
	}

	CreatureType(ICreatureBehaviour behaviour) {
		this.behaviour = behaviour;
	}

	public static CreatureType byOrdinal(int ordinal) {
		return values()[ordinal];
	}

	public ICreatureBehaviour getBehaviour() {
		return behaviour;
	}

}
