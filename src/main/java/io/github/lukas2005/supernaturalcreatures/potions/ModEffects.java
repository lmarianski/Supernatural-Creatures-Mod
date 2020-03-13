package io.github.lukas2005.supernaturalcreatures.potions;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.awt.*;
import java.util.ArrayList;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModEffects {

	private static ArrayList<Effect> effects = new ArrayList<>();

	@ObjectHolder("food_indigestion")
	public static final EffectBase FOOD_INDIGESTION = null;

	@ObjectHolder("photosensitivity")
	public static final EffectPhotosensitivity PHOTOSENSITIVITY = null;

	@ObjectHolder("wakefulness")
	public static final EffectPhotosensitivity WAKEFULNESS = null;

	public static void initEffects() {
		effects.add(new EffectBase("food_indigestion", EffectType.HARMFUL, new Color(125, 0, 5).getRGB()));
		effects.add(new EffectBase("wakefulness", EffectType.HARMFUL, new Color(21, 16, 125).getRGB()));
		effects.add(new EffectPhotosensitivity("photosensitivity", EffectType.HARMFUL, new Color(240, 255, 0).getRGB()));
	}

	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> e) {
		initEffects();
		e.getRegistry().registerAll(effects.toArray(new Effect[]{}));
		effects.clear();
	}

}
