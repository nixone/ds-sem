package sk.nixone.ds.core;

public abstract class EventProbabilityObserver<T> implements StaticSimulation.Observer<T> {
	
	private int totalTries = 0;
	private int successTries = 0;
	
	@Override
	public void doObserve(T result)
	{
		totalTries++;
		if(isSuccessfull(result)) {
			successTries++;
		}
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

	public abstract boolean isSuccessfull(T result);
}
