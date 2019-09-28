package io.github.lukas2005.supernaturalcreatures.render;

import com.mojang.blaze3d.platform.GlStateManager;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import io.github.lukas2005.supernaturalcreatures.render.player.RenderWerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.world.Pack;
import io.github.lukas2005.supernaturalcreatures.world.PackHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderHandler {

	public static RenderWerewolfPlayer WEREWOLF_RENDERER = null;

	@SubscribeEvent
	public static void onPlayerRender(RenderPlayerEvent.Pre e) {
		if (WEREWOLF_RENDERER == null) WEREWOLF_RENDERER = new RenderWerewolfPlayer(Minecraft.getInstance().getRenderManager(), false);
		PlayerEntity player = e.getPlayer();

		SCMPlayer playerData = SCMPlayer.of(player);
		playerData.getCreatureType().getBehaviour().playerRenderOverride(e, playerData);
	}

//	@SubscribeEvent
//	public static void onHandRender(RenderSpecificHandEvent e) {
//		if (WEREWOLF_RENDERER == null) WEREWOLF_RENDERER = new RenderWerewolfPlayer(Minecraft.getInstance().getRenderManager(), false);
//		PlayerEntity player = Minecraft.getInstance().player;
//
//		SCMPlayer playerData = SCMPlayer.of(player);
//		playerData.getCreatureType().getBehaviour().playerHandRenderOverride(e, playerData);
//	}

	private static final Minecraft mc = Minecraft.getInstance();

	private static void renderBox(BlockPos pos1, BlockPos pos2, double partialTicks, Color color, String text) {

		//Get player's actual position
		PlayerEntity player = mc.player;
		double x = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double y = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double z = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		//Render the box
		GlStateManager.pushMatrix();
		GlStateManager.enableAlphaTest();
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.lineWidth(5f);
		GlStateManager.disableTexture();
		GlStateManager.disableLighting();
		GlStateManager.translated(-x, -y, -z);
		float[] rgb = color.getRGBColorComponents(null);
		AxisAlignedBB box = new AxisAlignedBB(pos1, pos2.add(1, 1, 1)).grow(0.001d);
		GlStateManager.enableDepthTest();
		WorldRenderer.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, rgb[0], rgb[1], rgb[2], 0.2f);
		GlStateManager.disableDepthTest();
		WorldRenderer.drawSelectionBoundingBox(box, rgb[0], rgb[1], rgb[2], 0.4f);
		Vec3d playerPos = player.getEyePosition((float) partialTicks);
		Vec3d nameRenderPos = box.getCenter();

		if (playerPos.y < box.minY + 0.5d) {
			nameRenderPos = new Vec3d(nameRenderPos.x, box.minY + 0.5d, nameRenderPos.z);
		} else if(playerPos.y > box.maxY - 0.5d) {
			nameRenderPos = new Vec3d(nameRenderPos.x, box.maxY - 0.5d, nameRenderPos.z);
		} else {
			nameRenderPos = new Vec3d(nameRenderPos.x, playerPos.y, nameRenderPos.z);
		}

		if (text != null && !text.isEmpty()) renderText(text, nameRenderPos);
		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
		GlStateManager.enableDepthTest();
		GlStateManager.popMatrix();
	}

	//Copied a lot of this from EntityRenderer#drawNameplate and changed for my needs
	private static void renderText(String text, Vec3d center) {
		EntityRendererManager rm = mc.getRenderManager();
		float viewerYaw = rm.playerViewY;
		float viewerPitch = rm.playerViewX;
		boolean isThirdPersonFrontal = rm.options.thirdPersonView == 2;

		GlStateManager.translated(center.x, center.y, center.z);
		GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(-viewerYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
		float scale = 0.04f;
		GlStateManager.scalef(-scale, -scale, scale);
		GlStateManager.disableTexture();

		FontRenderer fr = mc.fontRenderer;

		int i = fr.getStringWidth(text) / 2;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(-i - 1, -1D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		buffer.pos(-i - 1, 8D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		buffer.pos(i + 1, 8D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		buffer.pos(i + 1, -1D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture();
		fr.drawString(text, -i, 0, -1);
		GlStateManager.disableBlend();
	}

	public static HashMap<UUID, Color> map = new HashMap<>();
	public static Random rand = new Random();


	@SubscribeEvent
	public static void areaRendering(RenderWorldLastEvent e) {
		PackHandler handler = PackHandler.forWorld(mc.world);

		if (Minecraft.getInstance().player.isSneaking()) {
			for (Map.Entry<ChunkPos, Pack> entry : handler.getPackChunks().entrySet()) {
				if (!map.containsKey(entry.getValue().id)) map.put(entry.getValue().id, new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
				ChunkPos pos = entry.getKey();
				renderBox(new BlockPos(pos.getXStart(), 0, pos.getZStart()), new BlockPos(pos.getXEnd(), 255, pos.getZEnd()), mc.getRenderPartialTicks(), map.get(entry.getValue().id), entry.getValue().id.toString());
			}
		}
	}
}
