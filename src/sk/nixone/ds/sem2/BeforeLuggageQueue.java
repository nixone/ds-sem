package sk.nixone.ds.sem2;

import sk.nixone.ds.core.time.SimulationRun;

public class BeforeLuggageQueue extends Queue<Luggage> {

	public BeforeLuggageQueue(Simulation simulation, int line) {
		super(simulation, line);
	}
	
	@Override
	public void onItemAdded(SimulationRun run, Luggage luggage) {
		luggage.waitingToBeScanned.started(run);
		checkFirst(run);
	}
	
	@Override
	public void onItemRemoved(SimulationRun run, Luggage luggage) {
		simulation.travellersWithLuggage.get(line).checkFirst(run);
	}
	
	protected void checkFirst(SimulationRun run) {
		if(isEmpty()) return;
		
		// we are not yet processing anything and we will have space after scanning
		if(simulation.processingLuggage[line] == null && !simulation.afterLuggageQueues.get(line).isFull()) {
			// add to process, plan finish and remove
			Luggage luggage = simulation.processingLuggage[line] = remove(run);
			
			luggage.waitingToBeScanned.ended(run);
			luggage.scanning.started(run);
			
			LuggageScanFinished finishEvent = new LuggageScanFinished(simulation, line);
			simulation.processingEvents[line] = run.plan(simulation.luggageScanDurationGenerator.next(), finishEvent);
		}
	}
}
