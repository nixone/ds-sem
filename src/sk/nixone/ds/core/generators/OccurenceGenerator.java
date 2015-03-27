package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

public class OccurenceGenerator implements Generator<Boolean> {

	private Random sequence;
	private double probability;
	
	public OccurenceGenerator(Random sequence, double probability) {
		this.sequence = sequence;
		this.probability = probability;
	}
	
	@Override
	public Boolean next() {
		return sequence.nextDouble() <= probability;
	}
}
