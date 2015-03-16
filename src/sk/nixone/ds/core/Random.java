package sk.nixone.ds.core;

/**
 * Wrapper pre ucely buduceho vyvoja, nakolko bude nutne implementovat generatory pre ine nez rovnomerne a normalne normovane rozdelenia.
 * 
 * @author nixone
 *
 */
public class Random extends java.util.Random {
	
	public Random() {
		super();
	}
	
	public Random(long seed) {
		super(seed);
	}
	
	/**
	 * 
	 * - Ak poissonovo popisuje pocetnost za jednotku casu, exponencionalne popisuje medzery medzi prichodmi.
	 * 
	 * @param lambda
	 * @return 
	 */
	public double nextExponential(double lambda) {
		double u = nextDouble();
		return -Math.log(u)/lambda;
	}
	
	public double nextTriangle(double left, double center, double right) {
		double u = nextDouble();
		double d = (center-left)/(right-left);
		
		if(u < d) {
			return left + Math.sqrt(u*(right-left)*(center-left));
		}
		return right - Math.sqrt((1-u)*(right-left)*(right-center));
	}
	
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
