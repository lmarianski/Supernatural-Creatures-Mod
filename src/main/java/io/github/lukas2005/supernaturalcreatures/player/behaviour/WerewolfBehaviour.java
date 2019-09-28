package io.github.lukas2005.supernaturalcreatures.player.behaviour;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.lukas2005.supernaturalcreatures.player.CreatureType;
import io.github.lukas2005.supernaturalcreatures.player.ICreatureData;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.EnumPackRank;
import io.github.lukas2005.supernaturalcreatures.potions.ModEffects;
import io.github.lukas2005.supernaturalcreatures.render.RenderHandler;
import io.github.lukas2005.supernaturalcreatures.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.TickEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class WerewolfBehaviour extends DefaultCreatureBehaviour {

	public static final ArrayList<ResourceLocation> eyeOverlays = new ArrayList<>();
	public static final ArrayList<ResourceLocation> skinOverlays = new ArrayList<>();

	public static final int EYE_OVERLAY_COUNT = 3;
	public static final int SKIN_OVERLAY_COUNT = 4;

	@Override
	public void onInfectedTick(SCMPlayer player, long infectedTicks) {
		World world = player.entity.world;
		PlayerEntity entity = player.entity;

		if (!world.isRemote) {
			BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);

			long time = world.getDayTime() % 24000;

			if (world.getMoonPhase() == 0 && time >= 13800 && time <= 22000 && world.canBlockSeeSky(pos)) {
				if (world.isRaining() || world.isThundering()) {
					world.getWorldInfo().setClearWeatherTime(1000);
					world.getWorldInfo().setRainTime(0);
					world.getWorldInfo().setThunderTime(0);
					world.getWorldInfo().setRaining(false);
					world.getWorldInfo().setThundering(false);
				}

				player.endInfection(true);
			}

			// TODO
			if (infectedTicks >= 2000 && infectedTicks % 2000 == 0) {
				switch ((int) (Math.random() * 4)) {
					case(0):
						entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 500, 1, false, false));
						break;
					case(1):
						entity.addPotionEffect(new EffectInstance(ModEffects.FOOD_INDIGESTION, 500, 1, false, false));
						entity.sendStatusMessage(new TranslationTextComponent("convert.werewolf.food_indigestion").applyTextStyles(TextFormatting.GRAY, TextFormatting.ITALIC), true);
						break;
					case(2):
						entity.addPotionEffect(new EffectInstance(ModEffects.WAKEFULNESS, 500, 1, false, false));
						entity.sendStatusMessage(new TranslationTextComponent("convert.werewolf.wakefulness").applyTextStyles(TextFormatting.GRAY, TextFormatting.ITALIC), true);
						break;
					case(3):
						//entity.addPotionEffect(new EffectInstance(ModEffects.PHOTOSENSITIVITY, 500, 1, false, false));
						entity.sendStatusMessage(new TranslationTextComponent("convert.werewolf").applyTextStyles(TextFormatting.GRAY, TextFormatting.ITALIC), true);
						break;
					case(4):
						break;
				}
			}
		}
	}

	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent e, SCMPlayer playerData) {
	}

	@Override
	public void onInfectionEnd(SCMPlayer player, boolean converted) {
	}

	@OnlyIn(Dist.CLIENT)
	public void playerRenderOverride(RenderPlayerEvent.Pre e, SCMPlayer player) {
		if (player.isTransformed()) {
			e.setCanceled(true);

			PlayerEntity entity = e.getPlayer();
			RenderHandler.WEREWOLF_RENDERER.doRender((AbstractClientPlayerEntity) e.getPlayer(), e.getX(), e.getY(), e.getZ(), entity.getYaw(e.getPartialRenderTick()), e.getPartialRenderTick());
		}
	}

	@OnlyIn(Dist.CLIENT)
	public <T extends AbstractClientPlayerEntity, M extends BipedModel<T>> void playerSkinOverlayRender(SCMPlayer playerData, T player, LivingRenderer<T, M> playerRenderer, float scale, float partialTicks) {
		WerewolfData data = (WerewolfData) playerData.getCreatureData(CreatureType.WEREWOLF);
		ResourceLocation eyes = eyeOverlays.get(playerData.isTransformed() ? data.packRank.ordinal() + 3 : data.packRank.ordinal());

		World world = player.getEntityWorld();

		if (player.shouldRenderSneaking()) {
			GlStateManager.translatef(0.0F, 0.2F, 0.0F);
		}

		if (eyes != null) {
			int blockLevel = world.getLightFor(LightType.BLOCK, new BlockPos(player.posX, player.posY, player.posZ));
			int skyLevel = world.getLightFor(LightType.SKY, new BlockPos(player.posX, player.posY, player.posZ));

			long time = world.getDayTime() % 24000;
			boolean isNighttime = time >= 13000 && time <= 23000;

			int lightLevel = Math.max(blockLevel, isNighttime ? 0 : skyLevel);

			if (playerData.isTransformed()){
				playerRenderer.bindTexture(eyes);
				playerRenderer.getEntityModel().bipedHead.render(scale);
			}

			RenderUtil.renderGlowing(playerRenderer, playerRenderer.getEntityModel().bipedHead, eyes, ((15 - lightLevel) / 15f) * 240f, player, scale, partialTicks);
		}
	}

	@Override
    public ICreatureData creatureDataFromNBT(INBT nbt) {
        return new WerewolfData().fromNBT(nbt);
    }

    public static class WerewolfData implements ICreatureData {

	    public EnumPackRank packRank = EnumPackRank.BETA;
		public ResourceLocation skin = skinOverlays.get(2);

        @Nonnull
        @Override
        public INBT toNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("packRank", packRank.ordinal());
            nbt.putString("skin", skin.toString());
            return nbt;
        }

        @Nonnull
        @Override
        public ICreatureData fromNBT(@Nullable INBT nbt) {
            if (nbt != null) {
                this.packRank = EnumPackRank.byOrdinal(((CompoundNBT) nbt).getInt("packRank"));
                this.skin = new ResourceLocation(((CompoundNBT) nbt).getString("skin"));
            }
            return this;
        }
    }

}
