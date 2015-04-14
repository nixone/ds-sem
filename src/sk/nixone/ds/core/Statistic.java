package sk.nixone.ds.core;

/**
 * The most basic specification of statistic, which is able to return only mean of the data
 * 
 * @author nixone
 *
 */
public interface Statistic {
	
	/**
	 * Returns the mean
	 * @return mean
	 */
	public double getMean();
}
