package sk.nixone.ds.core;

import java.util.LinkedList;

public class Statistic {
	
	private LinkedList<Double> values = new LinkedList<Double>();
	
	private double maximum = Double.MIN_VALUE;
	private double minimum = Double.MAX_VALUE;
	
	private double sum = 0;
	
	public void add(double value) {
		sum += value;
		values.add(value);
		
		maximum = Math.max(value, maximum);
		minimum = Math.min(value, minimum);
	}
	
	public double getMean() {
		return sum / values.size();
	}
	
	public double getStandardDeviation() {
		double sum = 0;
		double mean = getMean();
		for(double v : values) {
			double a = v-mean;
			sum += a*a;
		}
		return sum / values.size();
	}
	
	public double getStandardError() {
		return Math.sqrt(getStandardDeviation());
	}
	
	public double getMaximum() {
		return maximum;
	}
	
	public double getMinimum() {
		return minimum;
	}
}
