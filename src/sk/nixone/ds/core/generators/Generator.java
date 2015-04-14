package sk.nixone.ds.core.generators;

/**
 * Represents a generator of a specific type of return object. Usually used
 * to represent a specific configured random distribution sequence.
 * 
 * @author nixone
 *
 * @param <T> type of objects being generated
 */
public interface Generator<T> {
	
	/**
	 * Generates and returns new value from the sequence
	 * @return new value
	 */
	public T next();
}
