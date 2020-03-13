package io.github.lukas2005.supernaturalcreatures.render;

import io.github.lukas2005.supernaturalcreatures.enums.EnumForm;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.render.player.RenderWerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.world.Pack;
import io.github.lukas2005.supernaturalcreatures.world.PackHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderHandler {

	private static final Minecraft mc = Minecraft.getInstance();

	public static RenderWerewolfPlayer WEREWOLF_RENDERER = null;


	@SubscribeEvent
	public static void onPlayerRender(RenderPlayerEvent.Pre e) {
		if (WEREWOLF_RENDERER == null) WEREWOLF_RENDERER = new RenderWerewolfPlayer(Minecraft.getInstance().getRenderManager(), false);
		PlayerEntity player = e.getPlayer();

		WerewolfPlayer playerData = WerewolfPlayer.of(player);
		if (playerData.getForm() == EnumForm.HYBRID) {
			e.setCanceled(true);

			RenderHandler.WEREWOLF_RENDERER.render(
					(AbstractClientPlayerEntity)e.getPlayer(),
					e.getEntity().rotationYaw,
					e.getPartialRenderTick(),
					e.getMatrixStack(),
					e.getBuffers(),
					e.getLight());
		}
	}

//	@SubscribeEvent
//	public static void onHandRender(RenderSpecificHandEvent e) {
//		if (WEREWOLF_RENDERER == null) WEREWOLF_RENDERER = new RenderWerewolfPlayer(Minecraft.getInstance().getRenderManager(), false);
//		PlayerEntity player = Minecraft.getInstance().player;
//
//		SCMPlayer playerData = SCMPlayer.of(player);
//		playerData.getCreatureType().getBehaviour().playerHandRenderOverride(e, playerData);
//	}


	public static HashMap<UUID, Color> map = new HashMap<>();
	public static Random rand = new Random();



	@SubscribeEvent
	public static void areaRendering(RenderWorldLastEvent e) {
		PackHandler handler = PackHandler.forWorld(Minecraft.getInstance().world);

		if (Minecraft.getInstance().player.isCrouching()) {
			for (Map.Entry<ChunkPos, Pack> entry : handler.getPackChunks().entrySet()) {
				if (!map.containsKey(entry.getValue().id)) map.put(entry.getValue().id, new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
				ChunkPos pos = entry.getKey();
				RenderUtil.renderBox(new BlockPos(pos.getXStart(), 0, pos.getZStart()), new BlockPos(pos.getXEnd(), 255, pos.getZEnd()), mc.getRenderPartialTicks(), map.get(entry.getValue().id), entry.getValue().id.toString());
			}
		}
	}
}
