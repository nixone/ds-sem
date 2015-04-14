package sk.nixone.ds.core.generators;

import sk.nixone.ds.core.Random;

/**
 * Generator for triangle distribution.
 * 
 * @author nixone
 *
 */
public class TriangleGenerator implements Generator<Double> {

	private Random sequence;
	private double left, center, right;
	
	/**
	 * Creates a triangle distribution generator with specific parameters
	 * @param sequence random sequence to generate the values from
	 * @param left lower possible generated value
	 * @param center value with the "biggest probability" of happening (modus)
	 * @param right highest possible generated value
	 */
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
