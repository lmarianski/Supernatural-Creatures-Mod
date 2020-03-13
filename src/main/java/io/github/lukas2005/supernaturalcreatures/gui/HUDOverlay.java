package io.github.lukas2005.supernaturalcreatures.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HUDOverlay extends AbstractGui {

    private static Minecraft mc = Minecraft.getInstance();

    private static int screenColor = 0;
    private static int screenPercentage = 0;
    private static boolean fullScreen = false;
    private static int renderFullTick = 0;
    private static int renderFullOn, renderFullOff, renderFullColor;
    private static int screenBottomColor = 0;
    private static int screenBottomPercentage = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (mc.player != null && mc.player.isAlive() && e.phase == TickEvent.Phase.START) {
            //SCMPlayer player = SCMPlayer.of(mc.player);

        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre e) {
        if (mc.player == null || !mc.player.isAlive()) return;

        //SCMPlayer player = SCMPlayer.of(mc.player);

        if (e.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
        }
    }

}
