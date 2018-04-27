package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SundamageRegistry {

	List<Integer> noDamageDims = new ArrayList<>();
	List<Biome> noDamageBiomes = new ArrayList<>();

	public SundamageRegistry registerNoDamageBiome(Biome b) {
		noDamageBiomes.add(b);
		return this;
	}

	public SundamageRegistry registerNoDamageBiomes(Collection<Biome> b) {
		noDamageBiomes.addAll(b);
		return this;
	}

	public SundamageRegistry registerNoDamageDim(int b) {
		noDamageDims.add(b);
		return this;
	}

	public SundamageRegistry registerNoDamageDims(Collection<Integer> b) {
		noDamageDims.addAll(b);
		return this;
	}

	public boolean getSundamageInDim(int dimension) {
		return !noDamageDims.contains(dimension);
	}

	public boolean getSundamageInBiome(Biome biome) {
		return !noDamageBiomes.contains(biome);
	}
}
