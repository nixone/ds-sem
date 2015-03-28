package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.ProcessMarker;

public class Traveler {

	int line;
	
	Luggage luggage = null;
	
	ProcessMarker stayInSystem = new ProcessMarker();
	ProcessMarker waitingForLuggageHandover = new ProcessMarker();
	ProcessMarker waitingForCheckup = new ProcessMarker();
	ProcessMarker stayWithDetector = new ProcessMarker();
	ProcessMarker stayWithPersonalCheckup = new ProcessMarker();
	ProcessMarker waitingForLuggage = new ProcessMarker();
	
	public Traveler(int line, boolean hasLuggage) {
		this.line = line;
		if (hasLuggage) {
			luggage = new Luggage(this);
		}
	}
}
