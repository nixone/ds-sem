package sk.nixone.ds.core;

public interface ConfidenceStatistic extends Statistic {
	public int getSampleCount();
	public double getStandardDeviation();
}
