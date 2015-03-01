package sk.nixone.ds.sem1;

import sk.nixone.ds.core.ProbabilityObserver;

/**
 * Pozorovatel, ktoreho ulohou je sledovat stav simulacie, kedy je na konci jednej hry simulacie zivy len jeden konkretny hrac.
 * 
 * @author nixone
 *
 */
public class PlayerStayedAliveObserver extends ProbabilityObserver {

	private int n;
	private Game game;
	
	/**
	 * 
	 * @param game
	 * @param n index hraca, ktoreho prezitie na konci hry sledujeme
	 */
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
