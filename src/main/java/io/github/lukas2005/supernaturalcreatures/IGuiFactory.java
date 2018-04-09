package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IGuiFactory<O> {

	O newInstance(EntityPlayer player, World world, int x, int y, int z);

}
