package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

public class UniformGenerator implements Generator<Double> {

	private Random sequence;
	private double from, to;
	
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
