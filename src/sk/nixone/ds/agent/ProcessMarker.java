package sk.nixone.ds.agent;

import sk.nixone.ds.agent.SimulationRun;

public class ProcessMarker extends sk.nixone.ds.core.ProcessMarker {
	
	private double scheduledEnd;
	
	public void started(SimulationRun run) {
		super.started(run.currentTime());
	}
	
	public void started(SimulationRun run, double duration) {
		started(run);
		scheduledEnd(run.currentTime()+duration);
	}
	
	public void ended(SimulationRun run) {
		super.ended(run.currentTime());
	}
	
	public void scheduledEnd(double time) {
		scheduledEnd = time;
	}
	
	public double getProgress(double simulationTime) {
		if(!didHappen()) {
			return 0;
		}
		if(didFinish()) {
			return 1;
		}
		return (simulationTime-getStartTime()) / (scheduledEnd-getStartTime());
	}
	
	@Override
	public void reset() {
		super.reset();
		scheduledEnd = 0;
	}
}
