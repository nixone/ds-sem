package sk.nixone.ds.sem1;

import sk.nixone.ds.core.ProbabilityObserver;

public class AllDeadObserver extends ProbabilityObserver {

	private Game game;
	
	public AllDeadObserver(Game game) {
		this.game = game;
	}
	
	@Override
	public boolean isSuccessfull()
	{
		return game.areAllDead();
	}

}
