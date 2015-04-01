package sk.nixone.ds.core;

/**
 * Statistic able to count the average over some time, it depends on a time to start at 0.
 * 
 * @author nixone
 *
 */
public class TimeStatistic implements Statistic {

	private double sum = 0;
	private double weights = 0;
	private double lastTime = 0;
	
	/**
	 * Clears the statistic
	 */
	public void clear() {
		lastTime = sum = weights = 0;
	}
	
	/**
	 * Adds new record
	 * @param currentTime current time
	 * @param amount amount of something that is being observed
	 */
	public void add(double currentTime, double amount) {
		double weight = currentTime - lastTime;
		if(weight <= 0) {
			return;
		}
		
		sum += amount*weight;
		weights += weight;
		lastTime = currentTime;
	}
	
	/**
	 * Returns a mean of the observed values over time
	 * @return mean
	 */
	public double getMean() {
		if(weights <= 0) {
			return 0;
		}
		return sum/weights;
	}
}
