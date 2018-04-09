package io.github.lukas2005.supernaturalcreatures.gui;

import io.github.lukas2005.supernaturalcreatures.IGuiFactory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

public enum ModGui {

	SKILL_TREE((player, world, x, y, z) -> new GuiSkillTree(player), null);

	IGuiFactory<GuiScreen> client;
	IGuiFactory<Container> server;

	ModGui(IGuiFactory<GuiScreen> client, IGuiFactory<Container> server) {
		this.client = client;
		this.server = server;
	}

	public static ModGui byOrdinal(int ordinal) {
		return values()[ordinal];
	}
}
