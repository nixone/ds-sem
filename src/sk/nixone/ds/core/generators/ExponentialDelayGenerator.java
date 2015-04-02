package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

/**
 * Generator for exponential distribution with fixed lambda parameter.
 * 
 * @author nixone
 *
 */
public class ExponentialDelayGenerator implements Generator<Double> {
	
	private Random sequence;
	private double lambda;
	
	/**
	 * Creates specific exponential distribution generator with specific lambda parameter.
	 * @param sequence random sequence to generate the data from
	 * @param lambda distribution parameter
	 */
	public ExponentialDelayGenerator(Random sequence, double lambda) {
		this.sequence = sequence;
		this.lambda = lambda;
	}
	
	@Override
	public Double next() {
		return sequence.nextExponential(lambda);
	}
	
}
