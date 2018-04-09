package io.github.lukas2005.supernaturalcreatures.behaviour;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.enums.ResistanceLevel;
import io.github.lukas2005.supernaturalcreatures.skill.Level;
import io.github.lukas2005.supernaturalcreatures.skill.Skill;
import io.github.lukas2005.supernaturalcreatures.skill.SkillHelper;
import io.github.lukas2005.supernaturalcreatures.skill.SkillTree;
import io.github.lukas2005.supernaturalcreatures.skill.vampire.SkillHardTeeth;
import io.github.lukas2005.supernaturalcreatures.skill.vampire.SkillSunScreen;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;

public class VampireBehaviour extends DefaultCreatureBehaviour {

	public static final ArrayList<ResourceLocation> eyeOverlays = new ArrayList<>();
	public static final ArrayList<ResourceLocation> fangOverlays = new ArrayList<>();

	public static final int EYE_OVERLAY_COUNT = 1;
	public static final int FANG_OVERLAY_COUNT = 1;

	public VampireBehaviour() {
		super();
	}

	@Override
	public ResistanceLevel getResistanceLevel(DamageSource source) {
		return source.isFireDamage() ? ResistanceLevel.WEAKNESS : super.getResistanceLevel(source);
	}

	@Override
	public void applyWeakness(LivingHurtEvent hurtEvent, IPlayerDataCapability playerData, CreatureType type, EntityPlayer player) {
		if (hurtEvent.getSource().isFireDamage()) {
			hurtEvent.setAmount(hurtEvent.getAmount()+5f);
		}
	}

	@Override
	public void playerSkinOverlayRender(IPlayerDataCapability playerData, AbstractClientPlayer player, RenderPlayer playerRenderer, float scale, float partialTicks) {
		int eyeType = Math.max(-1, Math.min(Integer.parseInt(playerData.getData("vampire.eyes")), eyeOverlays.size() - 1));
		int fangType = Math.max(-1, Math.min(Integer.parseInt(playerData.getData("vampire.fangs")), fangOverlays.size() - 1));

		ResourceLocation fangs = null;
		ResourceLocation eyes = null;

		if (fangType > -1) {
			fangs = fangOverlays.get(fangType);
		}
		if (eyeType > -1) {
			eyes = eyeOverlays.get(eyeType);
		}

		if (player.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		if (fangs != null) {
			playerRenderer.bindTexture(fangs);
			playerRenderer.getMainModel().bipedHead.render(scale);
		}

		if (eyes != null) {
			renderNormalEyes(playerRenderer, eyes, scale);
		}
	}

	private void renderNormalEyes(RenderPlayer playerRenderer, ResourceLocation eyeType, float scale) {
		playerRenderer.bindTexture(eyeType);
		playerRenderer.getMainModel().bipedHead.render(scale);
	}

	@Override
	public void initSkillTree(SkillTree tree) {
		Level lvl1 = tree.addLevel();

		Level lvl2 = tree.addLevel();

		try {
			SkillHelper.createSkillChain(SkillSunScreen.class, true, tree, (objects, skill) -> {
				objects[0]=(int)objects[0]+1;
				skill.setIcon(new ResourceLocation(Reference.MOD_ID, "textures/skills/vampire/test1.jpg"), 0, 0, 916, 873);
			}, 1);
			SkillHelper.createSkillChain(SkillHardTeeth.class, true, tree, (objects, skill) -> {
				objects[0]=(int)objects[0]+1;
				skill.setIcon(new ResourceLocation(Reference.MOD_ID, "textures/skills/vampire/test2.png"), 0, 0, 35, 35);
			}, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
