package sk.nixone.ds.core;

public class SequenceStatistic implements ConfidenceStatistic {
	
	private double maximum = Double.MIN_VALUE;
	private double minimum = Double.MAX_VALUE;
	
	private int samples = 0;
	private double sum = 0;
	private double sumSquared = 0;
	
	public void add(double value) {
		sum += value;
		sumSquared += value*value;
		samples++;
		
		maximum = Math.max(value, maximum);
		minimum = Math.min(value, minimum);
	}
	
	public double getStandardDeviation() {
		double a = sumSquared / samples;
		double b = sum / samples;
		return Math.sqrt(a-b*b);
	}
	
	public int getSampleCount() {
		return samples;
	}
	
	public void clear() {
		samples = 0;
		sum = 0;
		sumSquared = 0;
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
