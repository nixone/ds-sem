package sk.nixone.ds.core;

import java.util.HashSet;

/**
 * Collection of many emitters, which are represented as one big emitter.
 * The emits and resets are just redistributed to all of them, no magic.
 * 
 * @author nixone
 *
 * @param <T> type of objects
 */
public class Emitters<T> implements Emitter<T> {

	private HashSet<Emitter<T>> emitters = new HashSet<Emitter<T>>();
	
	@Override
	public void emit(T value) {
		for(Emitter<T> emitter : emitters) {
			try {
				emitter.emit(value);
			} catch(Throwable cause) {
				cause.printStackTrace();
			}
		}
	}
	
	@Override
	public void reset() {
		for(Emitter<T> emitter : emitters) {
			emitter.reset();
		}
	}
	
	/**
	 * Adds new emitter to this collection.
	 * 
	 * @param emitter to be added
	 */
	public void add(Emitter<T> emitter) {
		emitters.add(emitter);
	}
}
