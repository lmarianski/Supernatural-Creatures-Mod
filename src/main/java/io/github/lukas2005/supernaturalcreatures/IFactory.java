package io.github.lukas2005.supernaturalcreatures;

public interface IFactory<O> {

	O newInstance(Object...args);

}
