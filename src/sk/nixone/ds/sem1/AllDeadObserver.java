package sk.nixone.ds.sem1;

import sk.nixone.ds.core.EventProbabilityObserver;

public class AllDeadObserver extends EventProbabilityObserver<boolean[]>
{

	@Override
	public boolean isSuccessfull(boolean[] isAlive)
	{
		for(int i=0; i<isAlive.length; i++) {
			if(isAlive[i]) {
				return false;
			}
		}
		return true;
	}

}
