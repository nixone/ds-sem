package sk.nixone.ds.sem2;

public abstract class Event implements sk.nixone.ds.core.time.Event {

	final protected Simulation simulation;
	
	public Event(Simulation simulation) {
		this.simulation = simulation;
	}
}
