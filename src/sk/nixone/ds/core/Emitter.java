package sk.nixone.ds.core;

public interface Emitter<K, V> {
	public void reset();
	public void emit(K key, V value);
}
