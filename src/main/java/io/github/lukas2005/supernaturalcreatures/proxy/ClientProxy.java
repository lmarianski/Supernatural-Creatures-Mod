package io.github.lukas2005.supernaturalcreatures.proxy;

import io.github.lukas2005.supernaturalcreatures.entity.EntityVampire;
import io.github.lukas2005.supernaturalcreatures.entity.EntityWerewolf;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.VampireBehaviour;
import io.github.lukas2005.supernaturalcreatures.player.behaviour.WerewolfBehaviour;
import io.github.lukas2005.supernaturalcreatures.render.entity.EntityVampireRenderer;
import io.github.lukas2005.supernaturalcreatures.render.entity.EntityWerewolfRenderer;
import io.github.lukas2005.supernaturalcreatures.render.player.LayerSkinOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy implements IProxy {

    @Override
    public void setupClient(FMLClientSetupEvent e) {
        for (int i = 0; i < VampireBehaviour.EYE_OVERLAY_COUNT; i++) {
            VampireBehaviour.eyeOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/vampire/eyes/eyes" + i + ".png"));
        }

        for (int i = 0; i < VampireBehaviour.FANG_OVERLAY_COUNT; i++) {
            VampireBehaviour.fangOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/vampire/fangs/fangs" + i + ".png"));
        }

        for (int i = 0; i < WerewolfBehaviour.EYE_OVERLAY_COUNT; i++) {
            WerewolfBehaviour.eyeOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/werewolf/eyes/eyes" + i + ".png"));
        }

        for (int i = 0; i < WerewolfBehaviour.EYE_OVERLAY_COUNT; i++) {
            WerewolfBehaviour.eyeOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/werewolf/eyes/eyes" + i + "_ww.png"));
        }

        for (int i = 0; i < WerewolfBehaviour.SKIN_OVERLAY_COUNT; i++) {
            WerewolfBehaviour.skinOverlays.add(new ResourceLocation(Reference.MOD_ID + ":textures/entity/player/overlay/werewolf/skins/skin" + i + ".png"));
        }

        for (PlayerRenderer renderPlayer : Minecraft.getInstance().getRenderManager().getSkinMap().values()) {
            renderPlayer.addLayer(new LayerSkinOverlay<>(renderPlayer));
        }

        RenderingRegistry.registerEntityRenderingHandler(EntityVampire.class, EntityVampireRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWerewolf.class, EntityWerewolfRenderer::new);
    }
}
