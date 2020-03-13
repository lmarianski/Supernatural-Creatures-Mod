package io.github.lukas2005.supernaturalcreatures.items;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);

	private static ArrayList<Item> items = new ArrayList<>();

	public static final RegistryObject<TestItem> VAMP_TEST = ITEMS.register("test", TestItem::new);

	public static final RegistryObject<Item> SILVER_INGOT  = ITEMS.register("silver_ingot" , () -> new Item(new Properties().group(ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item(new Properties().group(ItemGroup.MATERIALS)));

	public static void initItems() {
	}

	@SubscribeEvent
	public static void onRegsterItemModels(ModelRegistryEvent e) {
//		for (Item item : items) {
//			if (item instanceof IHasModel) {
//				((IHasModel) item).registerModels();
//			} else {
//				//Main.proxy.registerItemModel(item, 0, "inventory");
//			}
//		}
	}
}
