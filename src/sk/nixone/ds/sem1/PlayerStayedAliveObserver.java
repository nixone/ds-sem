package sk.nixone.ds.sem1;

import sk.nixone.ds.core.ProbabilityObserver;

public class PlayerStayedAliveObserver extends ProbabilityObserver {

	private int n;
	private Game game;
	
	public PlayerStayedAliveObserver(Game game, int n) {
		this.n = n;
		this.game = game;
	}
	
	@Override
	public boolean isSuccessfull()
	{
		return game.isPlayerAlive(n);
	}
}
