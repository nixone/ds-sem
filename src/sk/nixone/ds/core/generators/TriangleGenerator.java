package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

public class TriangleGenerator implements Generator<Double> {

	private Random sequence;
	private double left, center, right;
	
	public TriangleGenerator(Random sequence, double left, double center, double right) {
		this.sequence = sequence;
		this.left = left;
		this.right = right;
		this.center = center;
	}
	
	@Override
	public Double next() {
		return sequence.nextTriangle(left, center, right);
	}

}
