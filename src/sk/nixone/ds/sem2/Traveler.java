package sk.nixone.ds.sem2;

public class Traveler {

	int line;
	
	Luggage luggage = null;
	
	public Traveler(int line, boolean hasLuggage) {
		this.line = line;
		if (hasLuggage) {
			luggage = new Luggage(this);
		}
	}
}
