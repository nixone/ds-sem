package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

/**
 * Generator for uniformly distributed numbers across certain interval.
 * 
 * @author nixone
 *
 */
public class UniformGenerator implements Generator<Double> {

	private Random sequence;
	private double from, to;
	
	/**
	 * Creates a uniform distribution generator
	 * @param sequence random sequence to generate the values from
	 * @param from lowest possible value
	 * @param to highest possible value (excluded, so all the generated numbers are strictly lower than this one)
	 */
	public UniformGenerator(Random sequence, double from, double to) {
		this.sequence = sequence;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public Double next() {
		return from + sequence.nextDouble()*(to-from);
	}
}
