package io.github.lukas2005.supernaturalcreatures.potions;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.ArrayList;

@Mod.EventBusSubscriber
@ObjectHolder(Reference.MOD_ID)
public class ModPotions {

	private static ArrayList<Potion> potions = new ArrayList<>();

	@ObjectHolder("sanguinare")
	public static final SanguinarePotion SANGUINARE = null;

	public static void initPotions() {
		potions.add(new SanguinarePotion());
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> e) {
		initPotions();
		e.getRegistry().registerAll(potions.toArray(new Potion[]{}));
	}

}
