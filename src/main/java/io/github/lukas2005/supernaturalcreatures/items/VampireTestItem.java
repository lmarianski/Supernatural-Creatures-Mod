package io.github.lukas2005.supernaturalcreatures.items;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class VampireTestItem extends Item {

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

		if (!worldIn.isRemote) {
			IPlayerDataCapability playerData = playerIn.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);

			if (playerData.getCreatureType() != CreatureType.VAMPIRE) {
				playerData.setCreatureType(CreatureType.VAMPIRE);
			} else {
				playerData.setCreatureType(CreatureType.HUMAN);
			}
			//playerData.setCreatureType(CreatureType.VAMPIRE);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}
