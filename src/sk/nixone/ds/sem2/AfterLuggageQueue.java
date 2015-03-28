package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class AfterLuggageQueue extends Queue<Luggage> {

	public AfterLuggageQueue(Simulation simulation, int line) {
		super(simulation, line);
	}

	@Override
	public void onItemAdded(SimulationRun run, Luggage luggage) {
		if(luggage.traveler.waitingForLuggage.didHappen()) {
			luggage.traveler.stayInSystem.ended(run);
			luggage.traveler.waitingForLuggage.ended(run);
			luggage.waitingToBePicked.ended(run);
			simulation.finish(luggage.traveler);
			remove(run, luggage);
		}
	}
	
	@Override
	public void onItemRemoved(SimulationRun run, Luggage luggage) {
		simulation.beforeLuggageQueues.get(line).checkFirst(run);
	}
}
