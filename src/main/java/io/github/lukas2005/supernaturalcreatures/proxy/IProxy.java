package io.github.lukas2005.supernaturalcreatures.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public interface IProxy {

    void setupClient(FMLClientSetupEvent e);

}
