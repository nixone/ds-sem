package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class TravelerWithLuggageQueue extends Queue<Traveler> {
	public TravelerWithLuggageQueue(Simulation simulation, int line) {
		super(simulation, line);
	}

	@Override
	public void add(SimulationRun run, Traveler traveler) {
		super.add(run, traveler);
		checkFirst();
	}
	
	private void checkFirst() {
		Traveler first = peek();
		if (first.luggage == null) {
			
		}
	}
}
