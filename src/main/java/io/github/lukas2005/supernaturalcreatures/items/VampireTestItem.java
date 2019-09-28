package io.github.lukas2005.supernaturalcreatures.items;

import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class VampireTestItem extends Item {

	public VampireTestItem() {
		super(new Properties());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
		SCMPlayer player = SCMPlayer.of(playerIn);

			if (playerIn.isSneaking() && hand == Hand.MAIN_HAND && !worldIn.isRemote) {

				player.setConversion(null);
				if (player.getCreatureType() == CreatureType.HUMAN) {
					//playerData.setCreatureType(CreatureType.VAMPIRE);
					player.convert(CreatureType.VAMPIRE, true);
				} else if (player.getCreatureType() == CreatureType.VAMPIRE) {
					player.convert(CreatureType.WEREWOLF, true);
				} else if (player.getCreatureType() == CreatureType.WEREWOLF) {
					player.convert(CreatureType.HUMAN, true);
				}
			} else if (hand == Hand.OFF_HAND && !worldIn.isRemote) {
				if (!playerIn.isSneaking()) {
					player.setLevel(player.getLevel() + 1);
				} else {
					player.setLevel(player.getLevel() - 1);
				}
				player.syncData(true);
			} else {
				//playerIn.sho(Main.INSTANCE, ModGui.SKILL_TREE.ordinal(), worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);

				Main.LOGGER.info(player.getConversion() + " " + player.getCreatureType() + " " + player.getLevel());
			}
		return ActionResult.newResult(ActionResultType.SUCCESS, playerIn.getHeldItem(hand));
	}
}
