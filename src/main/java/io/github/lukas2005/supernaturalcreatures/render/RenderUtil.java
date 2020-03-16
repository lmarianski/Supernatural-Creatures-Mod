package io.github.lukas2005.supernaturalcreatures.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

	private static final Minecraft mc = Minecraft.getInstance();

	public static void renderBox(MatrixStack matrix, BlockPos pos1, BlockPos pos2, double partialTicks, Color color, String text) {

		//Get player's actual position
		PlayerEntity player = mc.player;
		double x = player.prevPosX + (player.getPosX() - player.prevPosX) * partialTicks;
		double y = player.prevPosY + (player.getPosY() - player.prevPosY) * partialTicks;
		double z = player.prevPosZ + (player.getPosZ() - player.prevPosZ) * partialTicks;

		//Render the box
		matrix.push();
		{
			glEnable(GL_ALPHA_TEST);
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(
					GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param,
					GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param
			);
			GlStateManager.lineWidth(5f);
			GlStateManager.disableTexture();
			glDisable(GL_LIGHTING);
			matrix.translate(-x, -y, -z);
			float[] rgb = color.getRGBColorComponents(null);
			AxisAlignedBB box = new AxisAlignedBB(pos1, pos2.add(1, 1, 1)).grow(0.001d);
			GlStateManager.enableDepthTest();
			DebugRenderer.renderBox(box, rgb[0], rgb[1], rgb[2], 0.2f);
			GlStateManager.disableDepthTest();
			//WorldRenderer.drawBoundingBox(matrix, , box, rgb[0], rgb[1], rgb[2], 0.4f);
			Vec3d playerPos = player.getEyePosition((float) partialTicks);
			Vec3d nameRenderPos = box.getCenter();

			if (playerPos.y < box.minY + 0.5d) {
				nameRenderPos = new Vec3d(nameRenderPos.x, box.minY + 0.5d, nameRenderPos.z);
			} else if (playerPos.y > box.maxY - 0.5d) {
				nameRenderPos = new Vec3d(nameRenderPos.x, box.maxY - 0.5d, nameRenderPos.z);
			} else {
				nameRenderPos = new Vec3d(nameRenderPos.x, playerPos.y, nameRenderPos.z);
			}

			if (text != null && !text.isEmpty()) renderText(text, nameRenderPos);
			GlStateManager.enableTexture();
			glEnable(GL_LIGHTING);
			GlStateManager.enableDepthTest();
		}
		matrix.pop();
	}

	//Copied a lot of this from EntityRenderer#drawNameplate and changed for my needs
	public static void renderText(String text, Vec3d center) {
		EntityRendererManager rm = mc.getRenderManager();
		float viewerYaw = rm.info.getYaw();
		float viewerPitch = rm.info.getPitch();
		boolean isThirdPersonFrontal = rm.options.thirdPersonView == 2;

		glTranslated(center.x, center.y, center.z);
		glNormal3f(0.0F, 1.0F, 0.0F);
		glRotatef(-viewerYaw, 0.0F, 1.0F, 0.0F);
		glRotatef((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
		float scale = 0.04f;
		glScalef(-scale, -scale, scale);
		GlStateManager.disableTexture();

		FontRenderer fr = mc.fontRenderer;

		int i = fr.getStringWidth(text) / 2;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(-i - 1, -1D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		buffer.pos(-i - 1, 8D , 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		buffer.pos(i + 1 , 8D , 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		buffer.pos(i + 1 , -1D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture();
		fr.drawString(text, -i, 0, -1);
		GlStateManager.disableBlend();
	}

}