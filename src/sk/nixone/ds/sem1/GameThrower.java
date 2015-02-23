package sk.nixone.ds.sem1;

import sk.nixone.ds.core.StaticSimulation;

public class GameThrower implements StaticSimulation.Thrower<boolean[]>
{
	private Game game;
	
	public GameThrower(Game game) {
		this.game = game;
	}
	
	@Override
	public void reset() {
	}
	
	@Override
	public boolean[] doThrow() {
		game.reset();
		game.execute();
		return game.getResult();
	}
}
