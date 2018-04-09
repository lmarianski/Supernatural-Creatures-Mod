package io.github.lukas2005.supernaturalcreatures.behaviour;

import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.enums.BufType;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.enums.ResistanceLevel;
import io.github.lukas2005.supernaturalcreatures.skill.SkillTree;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class DefaultCreatureBehaviour implements CreatureBehaviour {

	SkillTree tree;

	public DefaultCreatureBehaviour() {
		this.tree = new SkillTree();
		initSkillTree(tree);
	}

	@Override
	public boolean shouldApplyBuf(BufType buf, EntityPlayer player, IPlayerDataCapability playerData, CreatureType type) {
		switch (buf) {
			case HEALTH_BOOST:
				return !type.doesTransform || playerData.isTransformed();
			case SPEED_BOOST:
				return true;
			case STRENGTH_BOOST:
				return true;
			case DIG_SPEED_BOOST:
				return !type.doesTransform || playerData.isTransformed();
			case KNOCKBACK:
				return !type.doesTransform || playerData.isTransformed();
			case KNOCKBACK_RESISTANCE:
				return !type.doesTransform || playerData.isTransformed();
			case JUMP_BOOST:
				return !type.doesTransform || playerData.isTransformed();
			case RESISTANCE:
				return !type.doesTransform || playerData.isTransformed();
			case FALL_DISTANCE:
				return !type.doesTransform || playerData.isTransformed();
			default:
				return false;
		}
	}

	@Override
	public ResistanceLevel getResistanceLevel(DamageSource source) {
		if (source instanceof EntityDamageSource || source.getDamageType().equals("thrown") || source.getDamageType().equals("mob") || source.getDamageType().equals("arrow")) {
			return ResistanceLevel.RESISTANT;
		}
		return ResistanceLevel.NONE;
	}

	@Override
	public void applyWeakness(LivingHurtEvent hurtEvent, IPlayerDataCapability playerData, CreatureType type, EntityPlayer player) {
	}

	@Override
	public void playerSkinOverlayRender(IPlayerDataCapability playerData, AbstractClientPlayer player, RenderPlayer playerRenderer, float scale, float partialTicks) {
	}

	@Override
	public void playerRenderOverride(IPlayerDataCapability playerData, EntityPlayer player, RenderLivingBase<?> renderer) {
	}

	@Override
	public float getMultiplierForBuf(BufType buf, EntityPlayer player, IPlayerDataCapability playerData, int level) {
		return 0;
	}

	@Override
	public SkillTree getSkillTree() {
		return tree;
	}

	@Override
	public void initSkillTree(SkillTree tree) {
	}

}
