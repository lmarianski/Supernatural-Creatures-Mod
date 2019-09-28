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
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.TickEvent;

import javax.annotation.Nullable;

public interface ICreatureBehaviour {

	boolean isShapeshifter();

	boolean shouldApplyBuff(BuffType buff, SCMPlayer player, CreatureType type);
	double getBuff(BuffType buff, SCMPlayer player);
	AttributeModifier.Operation getOperation(BuffType buff, SCMPlayer player);

	@OnlyIn(Dist.CLIENT)
	<T extends AbstractClientPlayerEntity, M extends BipedModel<T>> void playerSkinOverlayRender(SCMPlayer playerData, T player, LivingRenderer<T, M> playerRenderer, float scale, float partialTicks);
	@OnlyIn(Dist.CLIENT)
	void playerRenderOverride(RenderPlayerEvent.Pre e, SCMPlayer player);

	void onInfected(SCMPlayer player);

	/**
	 *
	 * @param player
	 * @param converted Whether or not the end of infection resulted in the entity being converted
	 */
	void onInfectionEnd(SCMPlayer player, boolean converted);

	void onInfectedTick(SCMPlayer player, long infectedTicks);
	void onPlayerTick(TickEvent.PlayerTickEvent e, SCMPlayer playerData);

	SkillTree getSkillTree();
	void initSkillTree(SkillTree tree);

	ICreatureData getData(SCMPlayer player);

	/**
	 * Creates a new ICreatureData instance from NBT. Passing in null should result in a new empty instance being created.
	 * @param nbt NBT data
	 * @return A new instance
	 */
	ICreatureData creatureDataFromNBT(@Nullable INBT nbt);
}
