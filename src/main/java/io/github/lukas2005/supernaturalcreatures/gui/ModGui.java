package io.github.lukas2005.supernaturalcreatures.gui;

import io.github.lukas2005.supernaturalcreatures.IGuiFactory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;

public enum ModGui {

//	SKILL_TREE((player, world, x, y, z) -> new GuiSkillTree(player), null);
;
	IGuiFactory<Screen> client;
	IGuiFactory<Container> server;

	ModGui(IGuiFactory<Screen> client, IGuiFactory<Container> server) {
		this.client = client;
		this.server = server;
	}

	public static ModGui byOrdinal(int ordinal) {
		return values()[ordinal];
	}
}
