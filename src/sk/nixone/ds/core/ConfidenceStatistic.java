package sk.nixone.ds.core;

/**
 * This marks possilibity to count confidence intervals from statistic
 * 
 * @author nixone
 *
 */
public interface ConfidenceStatistic extends Statistic {
	/**
	 * Returns a number of samples in the data set
	 * @return a number of samples in the data set
	 */
	public int getSampleCount();
	
	/**
	 * Returns the standard deviation of data
	 * @return standard deviation of data
	 */
	public double getStandardDeviation();
}
