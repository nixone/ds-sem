package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class TravelerArrived extends Event {

	public TravelerArrived(Simulation simulation) {
		super(simulation);
	}

	@Override
	public void execute(SimulationRun run) {
		// create new traveler and put him into shorter line
		int line = simulation.getShorterLineIndex();
		boolean hasLuggage = simulation.hasLuggageGenerator.next();
		Traveler traveler = new Traveler(line, hasLuggage);
		simulation.travellersWithLuggage.get(line).add(run, traveler);
		traveler.stayInSystem.started(run);
		
		// plan new arrival
		double delta = simulation.travelerArrivalGenerator.next();
		double total = run.getCurrentSimulationTime()+delta;
		if (total <= simulation.modelDuration) {
			simulation.arrivalEvent = run.plan(delta, new TravelerArrived(simulation));
		} else {
			simulation.arrivalEvent = null;
		}
	}
}
