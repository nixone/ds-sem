package sk.nixone.ds.core;

public class TimeStatistic implements Statistic {

	double sum = 0;
	double weights = 0;
	double lastTime = 0;
	
	public void clear() {
		lastTime = sum = weights = 0;
	}
	
	public void add(double currentTime, double amount) {
		double weight = currentTime - lastTime;
		if(weight <= 0) {
			return;
		}
		// TODO
		
		sum += amount*weight;
		weights += weight;
		lastTime = currentTime;
	}
	
	public double getMean() {
		if(weights <= 0) {
			return 0;
		}
		return sum/weights;
	}
}
