package sk.nixone.ds.core.statik;

import java.util.HashSet;

import javax.swing.SwingUtilities;

import sk.nixone.ds.core.Emitter;

/**
 * Pozorovatel priebehu urcitej hodnoty pocas simulacie simulacneho modelu.
 * 
 * @author nixone
 *
 * @param <T> typ hodnoty, ktora je sledovana a dalej prezentovana
 */
public abstract class Observer<T> {
	
	/**
	 * Aktualizuje pozorovatela, v tejto metode je potrebne implementovat zakladnu funkcionalitu, nakolko
	 * tato metoda je zarucene volana po kazdej replikacii simulacie
	 * 
	 * @param replicationIndex index replikacie, inak povedane, poradove cislo replikacie - 1
	 */
	public abstract void update(int replicationIndex);
	
	/**
	 * Indikuje pozorovatelovi, ze ma aktualizovat uzivatelske rozhranie.
	 * V tejto metode je obycajne vytvoreny prezentovatelny medzivysledok pre uzivatela a prezentovany
	 * vsetkym objektom zodpovednym za jeho zobrazenie pomocou metody <code>emitUI(...)</code>
	 * 
	 * @param replicationIndex
	 */
	public abstract void updateUI(int replicationIndex);
	
	private HashSet<Emitter<Integer, T>> uiEmitters = new HashSet<Emitter<Integer, T>>();
	
	/**
	 * Oznami vsetkym objektom zodpovednym za prezentovanie medzivysledkov na nastavenie do vychodzieho stavu.
	 */
	public void reset() {
		for(Emitter<Integer, T> emitter : uiEmitters) {
			emitter.reset();
		}
	}
	
	/**
	 * Prida objekt, ktory bude aktualizovany medzivysledkami simulacie
	 * 
	 * @param emitter spominany objekt
	 */
	public void addUIEmitter(Emitter<Integer, T> emitter) {
		uiEmitters.add(emitter);
	}
	
	/**
	 * Odstrani objekt, ktory uz nadalej nebude aktualizovany medzivysledkami simualcie
	 * @param emitter
	 */
	public void removeUIEmitter(Emitter<Integer, T> emitter) {
		uiEmitters.remove(emitter);
	}
	
	/**
	 * Metoda pre zaslanie medzivysledku simulacie vsetkym objektom, ktore maju o to zaujem.
	 * 
	 * @param replicationIndex
	 * @param value
	 */
	protected void emitUI(final int replicationIndex, final T value) {
		for(Emitter<Integer, T> emitter : uiEmitters) {
			emitter.emit(replicationIndex, value);
		}
	}
}
