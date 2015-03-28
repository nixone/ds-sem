package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.ProcessMarker;

public class Luggage {
	
	Traveler traveler;
	
	ProcessMarker waitingToBeScanned = new ProcessMarker();
	ProcessMarker scanning = new ProcessMarker();
	ProcessMarker waitingToBePicked = new ProcessMarker(); // TODO
	
	public Luggage(Traveler traveler) {
		this.traveler = traveler;
	}
}
