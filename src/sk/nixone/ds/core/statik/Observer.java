package sk.nixone.ds.core.statik;

import java.util.HashSet;

import javax.swing.SwingUtilities;

import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.Pair;

/**
 * Observer of simulation run.
 * 
 * @author nixone
 *
 * @param <T> type of observed value
 * @deprecated should be replaced by emitters
 */
// TODO should be replaced by emitters
public abstract class Observer<T> {
	
	/**
	 * Refreshes thje observer. This method should implement basic functionality since it
	 * is called after each simulation replication
	 * 
	 * @param replicationIndex index of replication, otherwise said, number of replication - 1
	 */
	public abstract void update(int replicationIndex);
	
	/**
	 * Indicates the observer, that he should update the user interface
	 * 
	 * Indikuje pozorovatelovi, ze ma aktualizovat uzivatelske rozhranie.
	 * V tejto metode je obycajne vytvoreny prezentovatelny medzivysledok pre uzivatela a prezentovany
	 * vsetkym objektom zodpovednym za jeho zobrazenie pomocou metody <code>emitUI(...)</code>
	 * 
	 * @param replicationIndex
	 */
	public abstract void updateUI(int replicationIndex);
	
	private HashSet<Emitter<Pair<Integer, T>>> uiEmitters = new HashSet<Emitter<Pair<Integer, T>>>();
	
	/**
	 * Oznami vsetkym objektom zodpovednym za prezentovanie medzivysledkov na nastavenie do vychodzieho stavu.
	 */
	public void reset() {
		for(Emitter<Pair<Integer, T>> emitter : uiEmitters) {
			emitter.reset();
		}
	}
	
	/**
	 * Prida objekt, ktory bude aktualizovany medzivysledkami simulacie
	 * 
	 * @param emitter spominany objekt
	 */
	public void addUIEmitter(Emitter<Pair<Integer, T>> emitter) {
		uiEmitters.add(emitter);
	}
	
	/**
	 * Odstrani objekt, ktory uz nadalej nebude aktualizovany medzivysledkami simualcie
	 * @param emitter
	 */
	public void removeUIEmitter(Emitter<Pair<Integer, T>> emitter) {
		uiEmitters.remove(emitter);
	}
	
	/**
	 * Metoda pre zaslanie medzivysledku simulacie vsetkym objektom, ktore maju o to zaujem.
	 * 
	 * @param replicationIndex
	 * @param value
	 */
	protected void emitUI(final int replicationIndex, final T value) {
		for(Emitter<Pair<Integer, T>> emitter : uiEmitters) {
			emitter.emit(new Pair<Integer, T>(replicationIndex, value));
		}
	}
}
