package sk.nixone.ds.core;

/**
 * Key value pair of anything
 * 
 * @author nixone
 *
 * @param <K> key type
 * @param <V> value type
 */
public class Pair<K, V> {

	final private K key;
	final private V value;
	
	/**
	 * Creates a new key value pair
	 * @param key key
	 * @param value value
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Returns a key
	 * @return key
	 */
	public K getKey() {
		return key;
	}
	
	/**
	 * Returns a value
	 * @return value
	 */
	public V getValue() {
		return value;
	}
}
