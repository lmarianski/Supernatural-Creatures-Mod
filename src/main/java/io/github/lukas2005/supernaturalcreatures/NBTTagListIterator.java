package io.github.lukas2005.supernaturalcreatures;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

import java.util.Iterator;

public class NBTTagListIterator implements Iterable<NBTBase>, Iterator<NBTBase> {

	NBTTagList list;

	int i = 0;

	public NBTTagListIterator(NBTTagList list) {
		this.list = list;
	}

	@Override
	public Iterator<NBTBase> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return list.tagCount()-(i+1) >= 0;
	}

	@Override
	public NBTBase next() {
		i++;
		return list.get(i);
	}
}
