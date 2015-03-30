package sk.nixone.ds.core;

public class SequenceStatistic implements Statistic {
	
	private double maximum = Double.MIN_VALUE;
	private double minimum = Double.MAX_VALUE;
	
	private int samples = 0;
	private double sum = 0;
	
	public void add(double value) {
		sum += value;
		samples++;
		
		maximum = Math.max(value, maximum);
		minimum = Math.min(value, minimum);
	}
	
	public void clear() {
		samples = 0;
		sum = 0;
		maximum = Double.MIN_VALUE;
		minimum = Double.MAX_VALUE;
	}
	
	public double getMean() {
		return sum / samples;
	}
	
	public double getMaximum() {
		return maximum;
	}
	
	public double getMinimum() {
		return minimum;
	}
}
