package sk.nixone.ds.core;

/**
 * Rozhranie popisujuce cokolvek, co je schopne prijmat pary klucov a im prisluchajucim hodnot.
 * 
 * @author nixone
 *
 * @param <K> typ kluca
 * @param <V> typ hodnot
 */
public interface Emitter<K, V> {
	/**
	 * Indikuje novu mnozinu prichadzajucich parov.
	 */
	public void reset();
	
	/**
	 * Indikuje novy par.
	 * 
	 * @param key kluc
	 * @param value hodnota
	 */
	public void emit(K key, V value);
}
