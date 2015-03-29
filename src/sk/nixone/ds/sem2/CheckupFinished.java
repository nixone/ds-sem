package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class CheckupFinished extends Event {

	private int line;
	
	public CheckupFinished(Simulation simulation, int line) {
		super(simulation);
		this.line = line;
	}

	@Override
	public void execute(SimulationRun run) {
		simulation.detectorEvents[line] = null;
		Traveler traveler = simulation.checkingTravelers[line];
		boolean personal = simulation.needsPersonalCheckupGenerator.next();
		traveler.stayWithDetector.ended(run);
		
		// proceed to personal checkup
		if (personal) {
			traveler.stayWithPersonalCheckup.started(run);
			PersonalCheckupFinished event = new PersonalCheckupFinished(simulation, line);
			simulation.checkupEvents[line] = run.plan(simulation.personalCheckupDurationGenerator.next(), event);
		// finishing the traveler
		} else {
			traveler.waitingForLuggage.started(run);
			// if we already did scan his luggage, finish him
			if(traveler.luggage != null && traveler.luggage.waitingToBePicked.didHappen()) {
				simulation.afterLuggageQueues.get(line).remove(run, traveler.luggage);
				traveler.stayInSystem.ended(run);
				traveler.waitingForLuggage.ended(run);
				traveler.luggage.waitingToBePicked.ended(run);
				simulation.finish(traveler);
			} else if(traveler.luggage == null) {
				traveler.stayInSystem.ended(run);
				traveler.waitingForLuggage.ended(run);
				simulation.finish(traveler);
			}
			
			// clean the room for next one
			simulation.checkingTravelers[line] = null;
			simulation.beforeCheckupQueues.get(line).checkFirst(run);
		}
	}
}
