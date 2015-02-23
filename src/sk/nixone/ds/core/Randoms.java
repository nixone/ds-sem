package sk.nixone.ds.core;

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
