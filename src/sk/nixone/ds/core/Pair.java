package sk.nixone.ds.core;

public class Pair<K, V> {

	final public K key;
	final public V value;
	
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue() {
		return value;
	}
}
