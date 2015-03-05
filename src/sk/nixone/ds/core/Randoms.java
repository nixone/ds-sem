package sk.nixone.ds.core;

/**
 * Generator generatorov, ktoreho instancia by v ramci aplikacie mala byt pre ucely statistickych pravidiel vytvorena len jedna. Ostatne
 * generatory su nasledne generovane za pomoci tohto generatora.
 * 
 * @author nixone
 *
 */
public class Randoms
{
	private Random seedGenerator;
	
	public Randoms() {
		seedGenerator = new Random();
	}
	
	public Randoms(long seed) {
		seedGenerator = new Random(seed);
	}
	
	public Random getNextRandom() {
		return new Random(seedGenerator.nextLong());
	}
}
