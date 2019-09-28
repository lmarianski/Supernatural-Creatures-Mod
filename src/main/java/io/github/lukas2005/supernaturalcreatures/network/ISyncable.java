package io.github.lukas2005.supernaturalcreatures.network;

import net.minecraft.util.ResourceLocation;

public interface ISyncable {

    ResourceLocation getCapKey();

    int getEntityID();

}
