package sk.nixone.ds.core;

public class ConfidenceInterval {
	static public final double ALPHA_95 = 1.645;
	
	static public ConfidenceInterval count(double alpha, ConfidenceStatistic statistic) {
		double b = (alpha * statistic.getStandardDeviation()) / Math.sqrt(statistic.getSampleCount()-1);
		if(Double.isNaN(b) || Double.isInfinite(b)) {
			b = 0;
		}
		return new ConfidenceInterval(statistic.getMean()-b, statistic.getMean()+b);
	}
	
	private double bottomBound;
	private double topBound;
	
	protected ConfidenceInterval(double bottomBound, double topBound) {
		this.bottomBound = bottomBound;
		this.topBound = topBound;
	}
	
	public double getBottomBound() {
		return bottomBound;
	}
	
	public double getTopBound() {
		return topBound;
	}
}
