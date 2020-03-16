package io.github.lukas2005.supernaturalcreatures.proxy;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.entity.ModEntities;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.render.entity.EntityWerewolfRenderer;
import io.github.lukas2005.supernaturalcreatures.render.player.LayerSkinOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientProxy implements IProxy {
    @Override
    public World getNetworkWorld(NetworkEvent.Context ctx) {
        return Minecraft.getInstance().world;
    }

    @Override
    public void setup(FMLCommonSetupEvent e) {
        for (int i = 0; i < WerewolfPlayer.EYE_OVERLAY_COUNT; i++) {
            WerewolfPlayer.eyeOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/werewolf/eyes/eyes" + i + ".png"));
        }

        for (int i = 0; i < WerewolfPlayer.EYE_OVERLAY_COUNT; i++) {
            WerewolfPlayer.eyeOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/werewolf/eyes/eyes" + i + "_ww.png"));
        }

        for (int i = 0; i < WerewolfPlayer.SKIN_OVERLAY_COUNT; i++) {
            WerewolfPlayer.skinOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/werewolf/skins/skin" + i + ".png"));
        }

        for (PlayerRenderer renderPlayer : Minecraft.getInstance().getRenderManager().getSkinMap().values()) {
            renderPlayer.addLayer(new LayerSkinOverlay<>(renderPlayer));
        }

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.WEREWOLF.get(), EntityWerewolfRenderer::new);
    }
}
