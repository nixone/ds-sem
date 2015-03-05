package sk.nixone.ds.core.statik;

import java.util.Collection;
import java.util.HashSet;

import javax.swing.SwingUtilities;

/**
 * Zaklad statickej simulacie, ktora sa od ostatnych odlisuje hlavne absenciou casu pocas priebehu simulacie.
 * Pred simulovanim je mozne statickej simulacii pridelit roznych pozorovatelov stavu simulacie, ktori su automaticky
 * aktualizovany podla momentalneho priebehu simulacie.
 * 
 * @author nixone
 *
 */
public abstract class StaticSimulation {
	
	private Collection<ValueObserver<?>> observers = new HashSet<ValueObserver<?>>();
	
	/**
	 * Indikuje zaciatok novej simulacie, a teda resetuje vsetkych pozorovatelov simulacie 
	 */
	private void reset() {
		for(ValueObserver<?> observer : observers) {
			observer.reset();
		}
	}
	
	/**
	 * Metoda zodpovedna za vykonanie jednej konkretnej replikacie simulacneho modelu
	 */
	public abstract void runReplication();
	
	/**
	 * Prida do systemu pozorovatela priebehu
	 * 
	 * @param observer
	 */
	public void addObserver(ValueObserver<?> observer) {
		observers.add(observer);
	}
	
	/**
	 * Vymaze zo systemu pozorovatela priebehu
	 * @param observer
	 */
	public void removeObserver(ValueObserver<?> observer) {
		observers.remove(observer);
	}
	
	/**
	 * Vykona jeden priebeh simulacie
	 * 
	 * @param replications pocet replikacii simulacneho modelu, ktore sa maju spracovat
	 * @param updateUIEvery perioda replikacii, po ktorych sa periodicky aktualizuje uzivatleske rozhranie
	 * @param cropReplications pocet replikacii, ktore nie su od zaciatku simulacie uzivatelskemu rozhraniu prezentovane
	 */
	public void run(int replications, int updateUIEvery, int cropReplications) {
		reset();
		for(int replication=0; replication<replications; replication++) {
			runReplication();
			updateObservers(replication);
			if(replication % updateUIEvery == 0 && replication >= cropReplications) {
				updateObserversUI(replication);
			}
		}
	}
	
	private void updateObservers(int r) {
		for(ValueObserver<?> observer : observers) {
			observer.update(r);
		}
	}
	
	private void updateObserversUI(int r) {
		for(ValueObserver<?> o : observers) {
			o.updateUI(r);
		}
	}
}
