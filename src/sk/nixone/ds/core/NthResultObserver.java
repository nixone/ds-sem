package sk.nixone.ds.core;

import sk.nixone.ds.core.StaticSimulation.Observer;

public abstract class NthResultObserver<T> implements Observer<T> {

	private int n;
	private int seenResults = 0;
	
	public NthResultObserver(int n) {
		this.n = n;
	}
	
	@Override
	public void doObserve(T result)
	{
		seenResults++;
		if(seenResults % n == 0) {
			onNthResult(seenResults, result);
		}
	}
	
	@Override
	public void reset(){
		seenResults = 0;
	}
	
	public abstract void onNthResult(int realCountOfResults, T result);
}
