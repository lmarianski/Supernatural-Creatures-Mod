package io.github.lukas2005.supernaturalcreatures.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukas2005.supernaturalcreatures.entity.EntityOmegaWerewolf;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;

public class WerewolfEyesLayer<T extends EntityOmegaWerewolf, M extends BipedModel<T>> extends LayerRenderer<T, M> {

    protected LivingRenderer<T, M> renderer;

    public WerewolfEyesLayer(LivingRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, T player, float v, float v1, float v2, float v3, float v4, float v5) {
//        ResourceLocation eyes = WerewolfBehaviour.eyeOverlays.get(entityIn.getDataManager().get(EntityWerewolf.PACK_RANK)+3);
//        boolean isTransformed = entityIn.getDataManager().get(EntityWerewolf.IS_TRANSFORMED);
//
//        World world = entityIn.getEntityWorld();
//
//        if (entityIn.shouldRenderSneaking()) {
//            glTranslatef(0.0F, 0.2F, 0.0F);
//        }
//
//        if (eyes != null) {
//            int blockLevel = world.getLightFor(LightType.BLOCK, p);
//            int skyLevel = world.getLightFor(LightType.SKY, new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ));
//
//            long time = world.getDayTime() % 24000;
//            boolean isNighttime = time >= 13000 && time <= 23000;
//
//            int lightLevel = Math.max(blockLevel, isNighttime ? 0 : skyLevel);
//
//            if (isTransformed){
//                renderer.bindTexture(eyes);
//                renderer.getEntityModel().bipedHead.render(scale);
//            }
//
//            RenderUtil.renderGlowing(renderer, renderer.getEntityModel().bipedHead, eyes, ((15 - lightLevel) / 15f) * 240f, entityIn, scale, partialTicks);
//        }
    }

}
