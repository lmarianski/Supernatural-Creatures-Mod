package io.github.lukas2005.supernaturalcreatures.render.player;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class LayerSkinOverlay<T extends AbstractClientPlayerEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {
    private final LivingRenderer<T, M> playerRenderer;

    public LayerSkinOverlay(LivingRenderer<T, M> playerRendererIn) {
        super(playerRendererIn);
        this.playerRenderer = playerRendererIn;
    }


    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        WerewolfPlayer data = WerewolfPlayer.of(player);
        ResourceLocation eyes = WerewolfPlayer.eyeOverlays.get(data.isTransformed() ? data.getPackRank().ordinal() + 3 : data.getPackRank().ordinal());

        World world = player.getEntityWorld();

        if (eyes != null) {
            int blockLevel = world.getLightFor(LightType.BLOCK, player.getPosition());
            int skyLevel = world.getLightFor(LightType.SKY, player.getPosition());

            long time = world.getDayTime() % 24000;
            boolean isNighttime = time >= 13000 && time <= 23000;

            int lightLevel = Math.max(blockLevel, isNighttime ? 0 : skyLevel);

            float brightness = (15 - lightLevel) / 15f;

            if (data.isTransformed()) {
                IVertexBuilder vertexBuilder = iRenderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(eyes));
                this.getEntityModel().bipedHead.render(matrixStack, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            IVertexBuilder vertexBuilder = iRenderTypeBuffer.getBuffer(RenderType.getEyes(eyes));
            this.getEntityModel().bipedHead.render(matrixStack, vertexBuilder, 15728640, OverlayTexture.NO_OVERLAY, brightness, brightness, brightness, 1.0F);
        }
    }

}

//    WerewolfData data = (WerewolfData) playerData.getCreatureData(CreatureType.WEREWOLF);
//    ResourceLocation eyes = eyeOverlays.get(playerData.isTransformed() ? data.packRank.ordinal() + 3 : data.packRank.ordinal());
//
//    World world = player.getEntityWorld();
//
//                if (player.shouldRenderSneaking()) {
//                        GlStateManager.translatef(0.0F, 0.2F, 0.0F);
//                        }
//
//                        if (eyes != null) {
//                        int blockLevel = world.getLightFor(LightType.BLOCK, new BlockPos(player.posX, player.posY, player.posZ));
//                        int skyLevel = world.getLightFor(LightType.SKY, new BlockPos(player.posX, player.posY, player.posZ));
//
//                        long time = world.getDayTime() % 24000;
//                        boolean isNighttime = time >= 13000 && time <= 23000;
//
//                        int lightLevel = Math.max(blockLevel, isNighttime ? 0 : skyLevel);
//
//                        if (playerData.isTransformed()){
//                        playerRenderer.bindTexture(eyes);
//                        playerRenderer.getEntityModel().bipedHead.render(scale);
//                        }
//
//                        RenderUtil.renderGlowing(playerRenderer, playerRenderer.getEntityModel().bipedHead, eyes, ((15 - lightLevel) / 15f) * 240f, player, scale, partialTicks);
