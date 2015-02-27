package sk.nixone.ds.sem1;

import sk.nixone.ds.core.StaticSimulation;

public class GameSimulation extends StaticSimulation {
	
	private Game game;
	
	private AllDeadObserver allDeadObserver;
	private PlayerStayedAliveObserver [] playerObservers = new PlayerStayedAliveObserver[Game.PLAYER_COUNT];
	
	public GameSimulation(Game game) {
		this.game = game;
		
		allDeadObserver = new AllDeadObserver(game);		
		addObserver(allDeadObserver);
		for(int p=0; p<Game.PLAYER_COUNT; p++) {
			playerObservers[p] = new PlayerStayedAliveObserver(game, p);
			addObserver(playerObservers[p]);
		}
	}
	
	public AllDeadObserver getAllDeadObserver() {
		return allDeadObserver;
	}
	
	public PlayerStayedAliveObserver getPlayerStayedAliveObserver(int p) {
		return playerObservers[p];
	}

	public Game getGame() {
		return game;
	}
	
	@Override
	public void runReplication() {
		game.reset();
		game.execute();
	}
}
