package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class TravelerWithLuggageQueue extends Queue<Traveler> {
	public TravelerWithLuggageQueue(Simulation simulation, int line) {
		super(simulation, line);
	}
	
	@Override
	public void onItemAdded(SimulationRun run, Traveler traveler) {
		traveler.waitingForLuggageHandover.started(run);
		// we inserted first
		if(size() == 1) {
			checkFirst(run);
		}
	}
	
	@Override
	public void onItemRemoved(SimulationRun run, Traveler traveler) {
		traveler.waitingForLuggageHandover.ended(run);
		if(!isEmpty()) {
			checkFirst(run);
		}
	}
	
	protected void checkFirst(SimulationRun run) {
		Traveler traveler = peek();
		
		// we have luggage and there is place to put it
		if(traveler.luggage != null && !simulation.beforeLuggageQueues.get(line).isFull()) {
			// pass the luggage and proceed to checkup line
			simulation.beforeLuggageQueues.get(line).add(run, traveler.luggage);
			simulation.beforeCheckupQueues.get(line).add(run, traveler);
			
			remove(run);
		}
		// we don't have luggage, so just pass
		else if(traveler.luggage != null) {
			simulation.beforeCheckupQueues.get(line).add(run, traveler);
			
			remove(run);
		}
	}
}
