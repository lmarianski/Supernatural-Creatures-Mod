package io.github.lukas2005.supernaturalcreatures.player.behaviour;

import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import io.github.lukas2005.supernaturalcreatures.player.ICreatureData;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.enums.BuffType;
import io.github.lukas2005.supernaturalcreatures.skill.SkillTree;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.INBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.TickEvent;

public class DefaultCreatureBehaviour implements ICreatureBehaviour {

	SkillTree tree;

	public DefaultCreatureBehaviour() {
		this.tree = new SkillTree();
		initSkillTree(tree);
	}

//	@Override
//	public ResistanceLevel getResistanceLevel(DamageSource source) {
//		if (source instanceof EntityDamageSource || source.getDamageType().equals("thrown") || source.getDamageType().equals("mob") || source.getDamageType().equals("arrow")) {
//			return ResistanceLevel.RESISTANT;
//		}
//		return ResistanceLevel.NONE;
//	}

//	@Override
//	public void applyWeakness(LivingHurtEvent hurtEvent, IPlayerDataCapability playerData, CreatureType type, PlayerEntity player) {
//	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public <T extends AbstractClientPlayerEntity, M extends BipedModel<T>> void playerSkinOverlayRender(SCMPlayer playerData, T player, LivingRenderer<T, M> playerRenderer, float scale, float partialTicks) {
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void playerRenderOverride(RenderPlayerEvent.Pre e, SCMPlayer player) {
	}

	@Override
	public void onInfected(SCMPlayer player) {
		if (player.getConversion() != CreatureType.HUMAN) {
			player.addCreatureData(player.getConversion(), player.getConversion().getBehaviour().creatureDataFromNBT(null));
		}
	}

	@Override
	public void onInfectionEnd(SCMPlayer player, boolean converted) {

	}

	@Override
	public void onInfectedTick(SCMPlayer player, long infectedTicks) {
	}

	@Override
	public boolean isShapeshifter() {
		return false;
	}

	@Override
	public boolean shouldApplyBuff(BuffType buff, SCMPlayer player, CreatureType type) {

		ICreatureBehaviour behaviour = player.getCreatureType().getBehaviour();

		switch (buff) {
			case MAX_HEALTH:
			case FALL_DISTANCE:
			case DAMAGE_RESISTANCE:
			case JUMP_HEIGHT:
			case KNOCKBACK_RESISTANCE:
			case ATTACK_KNOCKBACK:
			case BREAK_SPEED:
				return !behaviour.isShapeshifter() || player.isTransformed();
			case MOVEMENT_SPEED:
			case ATTACK_DAMAGE:
				return true;
			default:
				return false;
		}
	}

	@Override
	public double getBuff(BuffType buff, SCMPlayer player) {
		return 0;
	}

	@Override
	public AttributeModifier.Operation getOperation(BuffType buff, SCMPlayer player) {
		return AttributeModifier.Operation.ADDITION;
	}

	@Override
	public SkillTree getSkillTree() {
		return tree;
	}

	@Override
	public void initSkillTree(SkillTree tree) {
	}

	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent e, SCMPlayer playerData) {
	}

	@Override
	public ICreatureData getData(SCMPlayer player) {
		return player.getCreatureData(player.getCreatureType());
	}

	@Override
	public ICreatureData creatureDataFromNBT(INBT nbt) {
		return null;
	}

}
