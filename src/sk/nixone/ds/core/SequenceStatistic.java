package sk.nixone.ds.core;

/**
 * Statistic able to tell information about the data.
 * This statistic is designed with constant memory requirements, so the amount of data
 * perceived is unbounded and any operation takes a constant amount of time.
 * 
 * @author nixone
 *
 */
public class SequenceStatistic implements ConfidenceStatistic {
	
	private double maximum = Double.MIN_VALUE;
	private double minimum = Double.MAX_VALUE;
	
	private int samples = 0;
	private double sum = 0;
	private double sumSquared = 0;
	
	/**
	 * Adds a new value to this dataset
	 * 
	 * @param value value
	 */
	public void add(double value) {
		sum += value;
		sumSquared += value*value;
		samples++;
		
		maximum = Math.max(value, maximum);
		minimum = Math.min(value, minimum);
	}
	
	/**
	 * Returns a standard deviation of the data
	 * @return standard deviation
	 */
	public double getStandardDeviation() {
		double a = sumSquared / samples;
		double b = sum / samples;
		return Math.sqrt(a-b*b);
	}
	
	/**
	 * Returns a sample count in this statistic
	 * @return sample count
	 */
	public int getSampleCount() {
		return samples;
	}
	
	/**
	 * Clears this statistic for new data. After this there is nothing left from preivous data.
	 */
	public void clear() {
		samples = 0;
		sum = 0;
		sumSquared = 0;
		maximum = Double.MIN_VALUE;
		minimum = Double.MAX_VALUE;
	}
	
	/**
	 * Returns a mean of the data
	 * @return mean
	 */
	public double getMean() {
		return sum / samples;
	}
	
	/**
	 * Returns a maximum data value. If there are no samples, it will return the minimum possible value perceivable.
	 * @return maximum data value
	 */
	public double getMaximum() {
		return maximum;
	}
	
	/**
	 * Returns a minimum data value. If there are no samples, it will return the maximum possible value preceivable.
	 * @return minimum data value
	 */
	public double getMinimum() {
		return minimum;
	}
}
