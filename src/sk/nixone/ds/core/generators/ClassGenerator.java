package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

public class ClassGenerator implements Generator<Integer> {

	private Random sequence;
	private double [] classProbabilities;
	
	public ClassGenerator(Random sequence, double... classProbabilities) {
		this.sequence = sequence;
		this.classProbabilities = classProbabilities;
	}
	
	@Override
	public Integer next() {
		return sequence.nextClass(classProbabilities);
	}
}
