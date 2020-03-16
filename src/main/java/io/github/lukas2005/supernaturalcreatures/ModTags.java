package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.item.Item;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.util.function.Predicate;

public class ModTags implements ISelectiveResourceReloadListener {

    private static ResourceLocation WEREWOLF_POISON_ID = new ResourceLocation(Reference.MOD_ID, "werewolf_poison");
    public static Tag<Item> WEREWOLF_POISON;

    public ModTags() {
        WEREWOLF_POISON = ItemTags.getCollection().getOrCreate(WEREWOLF_POISON_ID);
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> type) {
        WEREWOLF_POISON = ItemTags.getCollection().getOrCreate(new ResourceLocation("forge:ingots/silver"));
    }
}
