package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IGuiFactory<O> {

	O newInstance(PlayerEntity player, World world, int x, int y, int z);

}
