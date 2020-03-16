package io.github.lukas2005.supernaturalcreatures.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;

public class ServerProxy implements IProxy {
    @Override
    public World getNetworkWorld(NetworkEvent.Context ctx) {
        return Objects.requireNonNull(ctx.getSender()).world;
    }

    @Override
    public void setup(FMLCommonSetupEvent e) {
    }
}
