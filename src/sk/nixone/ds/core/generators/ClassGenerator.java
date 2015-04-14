package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

/**
 * Generator of class indices of certain probabilities
 * 
 * @author nixone
 *
 */
public class ClassGenerator implements Generator<Integer> {

	private Random sequence;
	private double [] classProbabilities;
	
	/**
	 * Creates a new class generator
	 * 
	 * @param sequence random sequence to use while generating the data (should be used alone for this)
	 * @param classProbabilities probabilities of classes (should add to 1 to work correctly!)
	 */
	public ClassGenerator(Random sequence, double... classProbabilities) {
		this.sequence = sequence;
		this.classProbabilities = classProbabilities;
	}
	
	@Override
	public Integer next() {
		return sequence.nextClass(classProbabilities);
	}
}
