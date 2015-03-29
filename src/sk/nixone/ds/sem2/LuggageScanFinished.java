package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class LuggageScanFinished extends Event {

	private int line;
	
	public LuggageScanFinished(Simulation simulation, int line) {
		super(simulation);
		this.line = line;
	}

	@Override
	public void execute(SimulationRun run) {
		simulation.processingEvents[line] = null;
		Luggage luggage = simulation.processingLuggage[line];
		luggage.scanning.ended(run);
		luggage.waitingToBePicked.started(run);
		simulation.processingLuggage[line] = null;
		simulation.afterLuggageQueues.get(line).add(run, luggage);
		simulation.beforeLuggageQueues.get(line).checkFirst(run);
	}
}
