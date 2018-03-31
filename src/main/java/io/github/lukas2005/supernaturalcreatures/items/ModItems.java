package io.github.lukas2005.supernaturalcreatures.items;

import io.github.lukas2005.supernaturalcreatures.IHasModel;
import io.github.lukas2005.supernaturalcreatures.Main;
import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ModItems {

	private static ArrayList<Item> items = new ArrayList<>();

	public static void initItems() {

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e) {
		initItems();
		e.getRegistry().registerAll(items.toArray(new Item[]{}));
	}

	@SubscribeEvent
	public static void onRegsterItemModels(ModelRegistryEvent e) {
		for (Item item : items) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			} else {
				Main.proxy.registerItemModel(item, 0, "inventory");
			}
		}
	}


	public static Item initHelper(Item item, String name) {
		items.add(item
				.setUnlocalizedName(Reference.MOD_ID+"."+name)
				.setRegistryName(new ResourceLocation(Reference.MOD_ID, name))
		);
		return item;
	}
}
