package io.github.lukas2005.supernaturalcreatures.player.behaviour;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.lukas2005.supernaturalcreatures.DamageSources;
import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.Utils;
import io.github.lukas2005.supernaturalcreatures.enums.BuffType;
import io.github.lukas2005.supernaturalcreatures.player.ICreatureData;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.potions.ModEffects;
import io.github.lukas2005.supernaturalcreatures.skill.SkillHelper;
import io.github.lukas2005.supernaturalcreatures.skill.tree.SkillTree;
import io.github.lukas2005.supernaturalcreatures.skill.vampire.SkillHardTeeth;
import io.github.lukas2005.supernaturalcreatures.skill.vampire.SkillSunScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class VampireBehaviour extends DefaultCreatureBehaviour {

	public static final ArrayList<ResourceLocation> eyeOverlays = new ArrayList<>();
	public static final ArrayList<ResourceLocation> fangOverlays = new ArrayList<>();

	public static final int EYE_OVERLAY_COUNT = 1;
	public static final int FANG_OVERLAY_COUNT = 1;

	public static final KeyBinding VAMPIRE_BITE = new KeyBinding("key."+Reference.MOD_ID+".vampire_bite", 229, Reference.NAME);

	public VampireBehaviour() {
		super();
	}

	@Override
	public double getBuff(BuffType buff, SCMPlayer player) {
		return buff == BuffType.MAX_HEALTH ? player.getLevel() * 0.5 : super.getBuff(buff, player);
	}

	@Override
	public void onInfectedTick(SCMPlayer player, long infectedTicks) {
		PlayerEntity entity = player.entity;

		// TODO
		if (!player.entity.getEntityWorld().isRemote && infectedTicks >= 2000 && infectedTicks % 2000 == 0) {
			switch ((int) (Math.random() * 4)) {
				case(0):
					entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 500, 1, false, false));
					break;
				case(1):
					entity.addPotionEffect(new EffectInstance(ModEffects.FOOD_INDIGESTION, 500, 1, false, false));
					entity.sendStatusMessage(new TranslationTextComponent("convert.vampire.food_indigestion").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC), true);
					break;
				case(2):
					entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 500, 1, false, false));
					entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 500, 1, false, false));
					entity.sendStatusMessage(new TranslationTextComponent("convert.vampire.weakness").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC), true);
					break;
				case(3):
					entity.addPotionEffect(new EffectInstance(ModEffects.PHOTOSENSITIVITY, 500, 1, false, false));
					entity.sendStatusMessage(new TranslationTextComponent("convert.vampire.photosensitivity").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC), true);
					break;
				case(4):
					break;
			}
		}
	}

	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent e, SCMPlayer player) {
		if (Utils.gettingSundamge(e.player)) {
			//if (playerData.hasData("vampire.sunDamage")) {
				e.player.attackEntityFrom(DamageSources.SUN, 1);
			//}
		}

		try {
			if (e.side == LogicalSide.CLIENT) {
				if (VAMPIRE_BITE.isKeyDown()) {
					Entity entity = (Entity) Minecraft.getInstance().objectMouseOver.hitInfo;
					Main.LOGGER.info(entity);
					if (entity != null) {
						//NetworkManager.INSTANCE.sendToServer(new DamageEntityMessage(e.player, entity, DamageSources.vampireBite(e.player), 4f));
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public <T extends AbstractClientPlayerEntity, M extends BipedModel<T>> void playerSkinOverlayRender(SCMPlayer playerData, T player, LivingRenderer<T, M> playerRenderer, float scale, float partialTicks) {
		VampireData data = (VampireData) getData(playerData);
		int eyeType = Math.max(0, Math.min(data.eyeType, eyeOverlays.size() - 1));
		int fangType = Math.max(0, Math.min(data.fangType, fangOverlays.size() - 1));

		ResourceLocation fangs = fangOverlays.get(fangType);
		ResourceLocation eyes = eyeOverlays.get(eyeType);

		if (player.shouldRenderSneaking()) {
			GlStateManager.translatef(0.0F, 0.2F, 0.0F);
		}

		if (fangs != null) {
			playerRenderer.bindTexture(fangs);
			playerRenderer.getEntityModel().bipedHead.render(scale);
		}

		if (eyes != null) {
			playerRenderer.bindTexture(eyes);
			playerRenderer.getEntityModel().bipedHead.render(scale);
		}
	}

	@Override
	public void initSkillTree(SkillTree tree) {
		Level lvl1 = tree.addLevel();

		Level lvl2 = tree.addLevel();

		Level lvl3 = tree.addLevel();

		Level lvl4 = tree.addLevel();

		try {
			SkillHelper.createSkillChain(SkillSunScreen.class, true, tree, (constructorParams, skill) -> {
				constructorParams[0]=(int)constructorParams[0]+1;
				skill.setIcon(new ResourceLocation(Reference.MOD_ID, "textures/skills/vampire/test1.jpg"), 0, 0, 916, 873);
			}, 1);
			SkillHelper.createSkillChain(SkillHardTeeth.class, true, tree, (constructorParams, skill) -> {
				constructorParams[0]=(int)constructorParams[0]+1;
				skill.setIcon(new ResourceLocation(Reference.MOD_ID, "textures/skills/vampire/test2.png"), 0, 0, 35, 35);
			}, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ICreatureData creatureDataFromNBT(INBT nbt) {
		return new VampireData().fromNBT(nbt);
	}

	public static class VampireData implements ICreatureData {

		int eyeType = 0;
		int fangType = 0;

		@Nonnull
		@Override
		public INBT toNBT() {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt("eyeType", eyeType);
			nbt.putInt("fangType", fangType);
			return nbt;
		}

		@Nonnull
		@Override
		public ICreatureData fromNBT(@Nullable INBT nbt) {
			if (nbt != null) {
				this.eyeType = ((CompoundNBT) nbt).getInt("eyeType");
				this.fangType = ((CompoundNBT) nbt).getInt("fangType");
			}
			return this;
		}
	}
}
