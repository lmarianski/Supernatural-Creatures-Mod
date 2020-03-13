package io.github.lukas2005.supernaturalcreatures.items;

import io.github.lukas2005.supernaturalcreatures.Main;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

	protected static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Main.MOD_ID);

	private static ArrayList<Item> items = new ArrayList<>();

	public static final VampireTestItem VAMP_TEST = ITEMS.register("vamp_test", VampireTestItem::new).get();

	public static final Item SILVER_INGOT  = ITEMS.register("vamp_test", () -> new Item(new Properties().group(ItemGroup.MATERIALS))).get();
	public static final Item SILVER_NUGGET = ITEMS.register("vamp_test", () -> new Item(new Properties().group(ItemGroup.MATERIALS))).get();

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
