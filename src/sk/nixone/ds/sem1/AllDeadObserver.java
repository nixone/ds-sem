package sk.nixone.ds.sem1;

import sk.nixone.ds.core.ProbabilityObserver;

/**
 * Pozorovatel, ktoreho ulohou je sledovat pritomnost stavu simulacie, kedy su vsetci hraci na konci hry mrtvi.
 * 
 * @author nixone
 *
 */
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
