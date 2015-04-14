package sk.nixone.ds.core;

/**
 * Interface describing anything that is able to percieve a sequence of objects
 * 
 * @author nixone
 *
 * @param <T> type of perceived objects
 */
public interface Emitter<T> {
	/**
	 * Indicates that the emitter should reset its internal state, if any. It indicates new independable set of objects being emitted.
	 */
	public void reset();
	
	/**
	 * Indicates new object on the sequence
	 * 
	 * @param value object
	 */
	public void emit(T value);
}
