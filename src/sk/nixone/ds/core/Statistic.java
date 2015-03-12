package sk.nixone.ds.core;

import java.util.LinkedList;

public class Statistic {
	
	private LinkedList<Double> values = new LinkedList<Double>();
	
	private double sum = 0;
	
	public void add(double value) {
		sum += value;
		values.add(value);
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
}
