package io.github.lukas2005.supernaturalcreatures.render;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class RenderUtil {

	/**
	 * Render the given model part using the given texture with a glowing lightmap (like vanilla spider)
	 *
	 * @param brightness Between 0 and 255f
	 */
	public static <T extends LivingEntity, M extends EntityModel<T>> void renderGlowing(LivingRenderer<T, M> render, RendererModel modelPart, ResourceLocation texture, float brightness, T entity, float scale, float partialTicks) {
		render.bindTexture(texture);

		startGlowing(entity.isInvisible(), brightness);
		modelPart.render(scale);
		endGlowing(entity.getBrightnessForRender());
	}

	/**
	 * Render the complete model using the given texture with a glowing lightmap (like vanilla spider)
	 *
	 * @param brightness Between 0 and 255f
	 */
	public static <T extends LivingEntity, M extends EntityModel<T>> void renderGlowing(LivingRenderer<T, M> render, ResourceLocation texture, float brightness, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		render.bindTexture(texture);
		render.bindTexture(texture);
		startGlowing(entity.isInvisible(), brightness);
		render.getEntityModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		endGlowing(entity.getBrightnessForRender());
	}

	private static void startGlowing(boolean entityInvisible, float brightness){
		GlStateManager.enableBlend();
		GlStateManager.enableAlphaTest();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

		if (entityInvisible) {
			GlStateManager.depthMask(false);
		} else {
			GlStateManager.depthMask(true);
		}


		float j = brightness % 65536;
		float k = brightness / 65536;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, j, k);

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getInstance().gameRenderer.setupFogColor(true);
	}

	private static void endGlowing(int brightnessForRender){
		Minecraft.getInstance().gameRenderer.setupFogColor(true);
		int i = brightnessForRender;
		int j = i % 65536;
		int k = i / 65536;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
		GlStateManager.disableBlend();
	}


}