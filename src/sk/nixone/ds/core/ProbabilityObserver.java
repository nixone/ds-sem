package sk.nixone.ds.core;

public abstract class ProbabilityObserver extends ValueObserver<Double> {
	
	private int totalTries = 0;
	private int successTries = 0;
	
	@Override
	public void update(int replicationIndex) {
		totalTries++;
		if(isSuccessfull()) {
			successTries++;
		}
	}
	
	@Override
	public void updateUI(int replicationIndex) {
		emitUI(replicationIndex, getProbability());
	}
	
	@Override
	public void reset() {
		totalTries = successTries = 0;
	}
	
	public double getProbability() {
		if(totalTries > 0) {
			return (double)successTries/totalTries;
		}
		return Double.NaN;
	}

	public abstract boolean isSuccessfull();
}
