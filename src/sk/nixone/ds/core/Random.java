package sk.nixone.ds.core;

/**
 * Random with more custom implemented generating methods.
 * 
 * @author nixone
 *
 */
public class Random extends java.util.Random {
	
	/**
	 * Creates a standard random with a seed generated from system time
	 */
	public Random() {
		super();
	}
	
	/**
	 * Creates a random with a seed passed as an argument
	 * @param seed seed
	 */
	public Random(long seed) {
		super(seed);
	}
	
	/**
	 * Generates a new value from exponential distribution.
	 * 
	 * @param lambda distribution parameter lambda (mean(EXP) = 1/lambda)
	 * @return generated value
	 */
	public double nextExponential(double lambda) {
		double u = nextDouble();
		return -Math.log(u)/lambda;
	}
	
	/**
	 * Generates new value from triangle distribution.
	 * 
	 * @param left lowest possible value
	 * @param center modus of the distribution (the value with "the biggest probability")
	 * @param right highest possible value
	 * @return generated value
	 */
	public double nextTriangle(double left, double center, double right) {
		double u = nextDouble();
		double d = (center-left)/(right-left);
		
		if(u < d) {
			return left + Math.sqrt(u*(right-left)*(center-left));
		}
		return right - Math.sqrt((1-u)*(right-left)*(right-center));
	}
	
	/**
	 * Generates class index dependable on the probability of the classes
	 * @param probabilities probabilities of the classes (they must add to 1 to work correctly!)
	 * @return
	 */
	public int nextClass(double... probabilities) {
		double u = nextDouble();
		double cumulative = 0;
		
		for(int i=0; i<probabilities.length; i++) {
			cumulative += probabilities[i];
			if (cumulative >= u) {
				return i;
			}
		}
		return probabilities.length - 1;
	}
}
