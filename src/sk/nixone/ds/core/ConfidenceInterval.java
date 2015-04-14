package sk.nixone.ds.core;

/**
 * Structure for sole holding of information bounded to certain confidence interval
 * 
 * @author nixone
 *
 */
public class ConfidenceInterval {
	/**
	 * Constant used when counting confidence interval with 95% degree of dependability
	 */
	static public final double ALPHA_95 = 1.645;
	
	/**
	 * Returns a confidence interval resulting from a certain statistic
	 * 
	 * @param alpha constant representing certain degree of dependability
	 * @param statistic data from which to count the interval
	 * @return confidence interval
	 */
	static public ConfidenceInterval count(double alpha, ConfidenceStatistic statistic) {
		double b = (alpha * statistic.getStandardDeviation()) / Math.sqrt(statistic.getSampleCount()-1);
		if(Double.isNaN(b) || Double.isInfinite(b)) {
			b = 0;
		}
		return new ConfidenceInterval(statistic.getMean()-b, statistic.getMean()+b);
	}
	
	private double lowerBound;
	private double upperBound;
	
	protected ConfidenceInterval(double lowerBound, double upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	/**
	 * Returns the lower bound of this interval
	 * 
	 * @return the lower bound of this interval
	 */
	public double getLowerBound() {
		return lowerBound;
	}
	
	/**
	 * Returns the upper bound of this interval
	 * @return the upper bound of this interval
	 */
	public double getUpperBound() {
		return upperBound;
	}
}
