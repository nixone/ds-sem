package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

public class ExponentialDelayGenerator implements Generator<Double> {
	
	private Random sequence;
	private double lambda;
	
	public ExponentialDelayGenerator(Random sequence, double mean) {
		this.sequence = sequence;
		this.lambda = 1/mean;
	}
	
	@Override
	public Double next() {
		return sequence.nextExponential(lambda);
	}
	
}
