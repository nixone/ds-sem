package sk.nixone.ds.sem2.events;

import sk.nixone.ds.core.time.SimulationRun;
import sk.nixone.ds.sem2.Event;
import sk.nixone.ds.sem2.Simulation;

public class TravelerArrived extends Event {

	public TravelerArrived(Simulation simulation) {
		super(simulation);
	}

	@Override
	public void execute(SimulationRun run) {
	}
}
