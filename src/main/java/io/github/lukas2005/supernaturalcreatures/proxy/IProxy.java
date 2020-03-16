package io.github.lukas2005.supernaturalcreatures.proxy;

import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IProxy {

    World getNetworkWorld(NetworkEvent.Context ctx);

    void setup(FMLCommonSetupEvent e);
}
