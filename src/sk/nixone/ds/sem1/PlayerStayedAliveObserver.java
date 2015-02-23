package sk.nixone.ds.sem1;

import sk.nixone.ds.core.EventProbabilityObserver;

public class PlayerStayedAliveObserver extends EventProbabilityObserver<boolean[]> {

	private int n;
	
	public PlayerStayedAliveObserver(int n) {
		this.n = n;
	}
	
	@Override
	public boolean isSuccessfull(boolean[] result)
	{
		return result[n];
	}
}
