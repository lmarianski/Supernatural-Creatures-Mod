package io.github.lukas2005.supernaturalcreatures.items;

import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.gui.ModGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class VampireTestItem extends Item {

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

		if (playerIn.isSneaking()) {
			if (!worldIn.isRemote) {
				IPlayerDataCapability playerData = playerIn.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

				if (playerData.getCreatureType() != CreatureType.VAMPIRE) {
					playerData.setCreatureType(CreatureType.VAMPIRE);
				} else {
					playerData.setCreatureType(CreatureType.HUMAN);
				}
				playerData.syncData(playerIn);
			}
		} else {
			IPlayerDataCapability playerData = playerIn.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			playerIn.openGui(Main.INSTANCE, ModGui.SKILL_TREE.ordinal(), worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);

			Main.logger.info(playerData.getCreatureType());
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}
