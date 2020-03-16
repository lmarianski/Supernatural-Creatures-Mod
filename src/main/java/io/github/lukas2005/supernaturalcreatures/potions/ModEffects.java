package io.github.lukas2005.supernaturalcreatures.potions;

import io.github.lukas2005.supernaturalcreatures.Reference;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModEffects {

	public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, Reference.MOD_ID);

	public static final RegistryObject<EffectBase> SILVER_POISONING = EFFECTS.register(
			"silver_poisoning",
			() -> new EffectSilverPoisoning(EffectType.HARMFUL, new Color(192, 192, 192).getRGB())
	);


	public static final RegistryObject<EffectBase> FOOD_INDIGESTION = EFFECTS.register(
			"food_indigestion",
			() -> new EffectBase(EffectType.HARMFUL, new Color(125, 0, 5).getRGB())
	);

	public static final RegistryObject<EffectBase> WAKEFULNESS = EFFECTS.register(
			"wakefulness",
			() -> new EffectBase(EffectType.HARMFUL, new Color(21, 16, 125).getRGB())
	);

}
