package sk.nixone.ds.core.time;

import sk.nixone.ds.core.Emitters;

public abstract class Simulation {
	
	private Emitters<Object> startedEmitters = new Emitters<Object>();
	private Emitters<Object> endedEmitters = new Emitters<Object>();
	private Emitters<Integer> replicationStartedEmitters = new Emitters<Integer>();
	private Emitters<Integer> replicationEndedEmitters = new Emitters<Integer>();
	private Emitters<Object> simulationUpdatedEmitters = new Emitters<Object>();
	
	private SimulationRun currentSimulationRun = null;
	
	private int currentReplicationNumber = -1;

	public Emitters<Object> getStarted() {
		return startedEmitters;
	}
	
	public Emitters<Object> getEnded() {
		return endedEmitters;
	}
	
	public Emitters<Integer> getReplicationStarted() {
		return replicationStartedEmitters;
	}
	
	public Emitters<Integer> getReplicationEnded() {
		return replicationEndedEmitters;
	}
	
	public Emitters<Object> getSimulationUpdated() {
		return simulationUpdatedEmitters;
	}
	
	public abstract void initializeRun(SimulationRun run);
	
	public void run(SimulationConfig config) {
		dispatchSimulationStarted();
		for(currentReplicationNumber=0; currentReplicationNumber<config.getReplications(); currentReplicationNumber++) {
			currentSimulationRun = new SimulationRun(this);
			initializeRun(currentSimulationRun);
			
			if (!config.isIgnoreImmediateEmitters()) {
				dispatchReplicationStarted(currentReplicationNumber);
			}
			
			currentSimulationRun.run(config);
			
			if (!config.isIgnoreImmediateEmitters()) {
				dispatchReplicationEnded(currentReplicationNumber);
			}
			currentSimulationRun = null;
		}
		if (config.isIgnoreImmediateEmitters()) {
			dispatchReplicationStarted(currentReplicationNumber);
			if (config.isIgnoreRunImmediateEmitters()) {
				dispatchSimulationUpdated();
			}
			dispatchReplicationEnded(currentReplicationNumber);
		}
		dispatchSimulationEnded();
	}
	
	public int getCurrentReplicationNumber() {
		return currentReplicationNumber;
	}
	
	public SimulationRun getCurrentSimulationRun() {
		return currentSimulationRun;
	}
	
	private void dispatchSimulationStarted() {
		startedEmitters.reset();
		endedEmitters.reset();
		replicationStartedEmitters.reset();
		simulationUpdatedEmitters.reset();
		startedEmitters.emit(null);
	}
	
	private void dispatchReplicationStarted(int replicationIndex) {
		simulationUpdatedEmitters.reset();
		replicationStartedEmitters.emit(replicationIndex);
	}
	
	protected void dispatchSimulationUpdated() {
		simulationUpdatedEmitters.emit(null);
	}
	
	private void dispatchReplicationEnded(int replicationIndex) {
		replicationEndedEmitters.emit(replicationIndex);
	}
	
	private void dispatchSimulationEnded() {
		endedEmitters.emit(null);
	}
}
