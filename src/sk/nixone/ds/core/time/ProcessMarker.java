package sk.nixone.ds.core.time;

public class ProcessMarker {
	private boolean started = false;
	private boolean ended = false;
	private double startTime;
	private double endTime;
	
	public void started(SimulationRun run) {
		if(started) {
			throw new IllegalStateException("started==true");
		}
		started = true;
		startTime = run.getCurrentSimulationTime();
	}
	
	public void ended(SimulationRun run) {
		if(ended) {
			throw new IllegalStateException("ended==true");
		}
		ended = true;
		endTime = run.getCurrentSimulationTime();
	}
	
	public double getDuration() {
		return endTime-startTime;
	}
	
	public boolean didHappen() {
		return started;
	}
	
	public boolean didFinish() {
		return ended;
	}
	
	public double getStartTime() {
		return startTime;
	}
	
	public double getEndTime() {
		return endTime;
	}
}
