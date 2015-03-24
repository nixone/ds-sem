package sk.nixone.ds.core;

/**
 * Rozhranie popisujuce cokolvek, co je schopne prijmat akekolvek hodnoty
 * 
 * @author nixone
 *
 * @param <T> typ obejktu
 */
public interface Emitter<T> {
	/**
	 * Indikuje novu mnozinu prichadzajucich parov.
	 */
	public void reset();
	
	/**
	 * Indikuje novy objekt.
	 * 
	 * @param value objekt
	 */
	public void emit(T value);
}
