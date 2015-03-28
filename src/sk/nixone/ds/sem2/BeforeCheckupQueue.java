package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class BeforeCheckupQueue extends Queue<Traveler> {

	public BeforeCheckupQueue(Simulation simulation, int line) {
		super(simulation, line);
	}

	@Override
	public void onItemAdded(SimulationRun run, Traveler traveler) {
		traveler.waitingForCheckup.started(run);
		if(size() == 1) {
			checkFirst(run);
		}
	}
	
	@Override
	public void onItemRemoved(SimulationRun run, Traveler traveler) {
		traveler.waitingForCheckup.ended(run);
	}
	
	protected void checkFirst(SimulationRun run) {
		Traveler traveler = peek();
		// if we are not checking anybody
		if(simulation.checkingTravelers[line] == null) {
			traveler.stayWithDetector.started(run);
			
			// start checking up and plan the finish
			simulation.checkingTravelers[line] = traveler;
			CheckupFinished event = new CheckupFinished(simulation, line);
			run.plan(simulation.checkupDurationGenerator.next(), event);
			
			remove(run);
		}
	}
}
