package io.github.lukas2005.supernaturalcreatures.render.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.lukas2005.supernaturalcreatures.entity.EntityWerewolf;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.WerewolfBehaviour;
import io.github.lukas2005.supernaturalcreatures.render.RenderUtil;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class WerewolfEyesLayer<T extends EntityWerewolf, M extends BipedModel<T>> extends LayerRenderer<T, M> {

    protected LivingRenderer<T, M> renderer;

    public WerewolfEyesLayer(LivingRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
    }

    @Override
    public void render(T entityIn, float v, float v1, float partialTicks, float v3, float v4, float v5, float scale) {
        ResourceLocation eyes = WerewolfBehaviour.eyeOverlays.get(entityIn.getDataManager().get(EntityWerewolf.PACK_RANK)+3);
        boolean isTransformed = entityIn.getDataManager().get(EntityWerewolf.IS_TRANSFORMED);

        World world = entityIn.getEntityWorld();

        if (entityIn.shouldRenderSneaking()) {
            GlStateManager.translatef(0.0F, 0.2F, 0.0F);
        }

        if (eyes != null) {
            int blockLevel = world.getLightFor(LightType.BLOCK, new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ));
            int skyLevel = world.getLightFor(LightType.SKY, new BlockPos(entityIn.posX, entityIn.posY, entityIn.posZ));

            long time = world.getDayTime() % 24000;
            boolean isNighttime = time >= 13000 && time <= 23000;

            int lightLevel = Math.max(blockLevel, isNighttime ? 0 : skyLevel);

            if (isTransformed){
                renderer.bindTexture(eyes);
                renderer.getEntityModel().bipedHead.render(scale);
            }

            RenderUtil.renderGlowing(renderer, renderer.getEntityModel().bipedHead, eyes, ((15 - lightLevel) / 15f) * 240f, entityIn, scale, partialTicks);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
