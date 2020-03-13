package io.github.lukas2005.supernaturalcreatures.items;

import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TestItem extends Item {

	public TestItem() {
		super(new Properties());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		WerewolfPlayer player = WerewolfPlayer.of(playerIn);

		player.turnWerewolf();

		return ActionResult.resultSuccess(playerIn.getHeldItem(hand));
	}
}
