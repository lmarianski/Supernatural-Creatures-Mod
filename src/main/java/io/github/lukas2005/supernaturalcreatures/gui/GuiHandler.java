package io.github.lukas2005.supernaturalcreatures.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ModGui gui = ModGui.byOrdinal(ID);
		if (gui.server != null) {
			return gui.server.newInstance(player, world, x, y, z);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ModGui gui = ModGui.byOrdinal(ID);
		if (gui.client != null) {
			return gui.client.newInstance(player, world, x, y, z);
		}
		return null;
	}
}
