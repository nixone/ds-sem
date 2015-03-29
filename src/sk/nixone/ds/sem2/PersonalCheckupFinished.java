package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class PersonalCheckupFinished extends Event {

	private int line;
	
	public PersonalCheckupFinished(Simulation simulation, int line) {
		super(simulation);
		this.line = line;
	}

	@Override
	public void execute(SimulationRun run) {
		simulation.checkupEvents[line] = null;
		Traveler traveler = simulation.checkingTravelers[line];
		traveler.stayWithPersonalCheckup.ended(run);
		traveler.waitingForLuggage.started(run);
		
		// if we already did scan his luggage, finish him
		if(traveler.luggage != null && traveler.luggage.waitingToBePicked.didHappen()) {
			simulation.afterLuggageQueues.get(line).remove(run, traveler.luggage);
			traveler.waitingForLuggage.ended(run);
			traveler.stayInSystem.ended(run);
			traveler.luggage.waitingToBePicked.ended(run);
			simulation.finish(traveler);
		} else if(traveler.luggage == null) {
			traveler.waitingForLuggage.ended(run);
			traveler.stayInSystem.ended(run);
			simulation.finish(traveler);
		}
		
		// clean the room for next one
		simulation.checkingTravelers[line] = null;
		simulation.beforeCheckupQueues.get(line).checkFirst(run);
	}
}
