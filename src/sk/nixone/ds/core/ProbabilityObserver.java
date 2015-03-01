package sk.nixone.ds.core;

/**
 * Obecny sledovac priebehu urcitych dat zamerany na sledovanie pravdepodobnosti urcitej udalosti pocas simulacie.
 * 
 * @author nixone
 *
 */
public abstract class ProbabilityObserver extends ValueObserver<Double> {
	
	private int totalTries = 0;
	private int successTries = 0;
	
	@Override
	public void update(int replicationIndex) {
		totalTries++;
		if(isSuccessfull()) {
			successTries++;
		}
	}
	
	@Override
	public void updateUI(int replicationIndex) {
		emitUI(replicationIndex, getProbability());
	}
	
	@Override
	public void reset() {
		super.reset();
		totalTries = successTries = 0;
	}
	
	/**
	 * Zisti momentalnu odhadovanu pravdepodobnost vyskytu na zaklade pomeru vyskytu udalosti a celkoveho poctu udalosti.
	 * 
	 * @return
	 */
	public double getProbability() {
		if(totalTries > 0) {
			return (double)successTries/totalTries;
		}
		return Double.NaN;
	}

	/**
	 * Abstraktna metoda, ktora je zavolana v ramci kazdheo pokusu, ktora indikuje, ci je dana udalost v momentalnom stave simulacie pritomna.
	 * 
	 * @return
	 */
	public abstract boolean isSuccessfull();
}
