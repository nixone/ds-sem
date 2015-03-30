package sk.nixone.ds.core.time;

import sk.nixone.ds.core.Emitters;

public abstract class Simulation {
	
	private Emitters<Object> startedEmitters = new Emitters<Object>();
	private Emitters<Object> endedEmitters = new Emitters<Object>();
	private Emitters<Object> replicationStartedEmitters = new Emitters<Object>();
	private Emitters<Object> replicationEndedEmitters = new Emitters<Object>();
	private Emitters<Object> simulationUpdatedEmitters = new Emitters<Object>();
	
	private boolean running = false;
	
	private SimulationRun currentSimulationRun = null;
	
	private int currentReplicationNumber = -1;

	public Emitters<Object> getStarted() {
		return startedEmitters;
	}
	
	public Emitters<Object> getEnded() {
		return endedEmitters;
	}
	
	public Emitters<Object> getReplicationStarted() {
		return replicationStartedEmitters;
	}
	
	public Emitters<Object> getReplicationEnded() {
		return replicationEndedEmitters;
	}
	
	public Emitters<Object> getSimulationUpdated() {
		return simulationUpdatedEmitters;
	}
	
	public void run(SimulationConfig config) {
		running = true;
		dispatchSimulationStarted();
		onStarted();
		currentSimulationRun = null;
		for(currentReplicationNumber=0; running==true && currentReplicationNumber<config.getReplications(); currentReplicationNumber++) {
			SimulationRun newSimulationRun = new SimulationRun(this);
			replan(currentSimulationRun, newSimulationRun);
			currentSimulationRun = newSimulationRun;
			
			onReplicationStart(currentReplicationNumber);
			
			if (!config.isIgnoreImmediateEmitters()) {
				dispatchReplicationStarted(currentReplicationNumber);
			}
			
			currentSimulationRun.run(config);
			onReplicationEnd(currentReplicationNumber);
			
			if (!config.isIgnoreImmediateEmitters()) {
				dispatchReplicationEnded(currentReplicationNumber);
			}
		}
		if (config.isIgnoreImmediateEmitters()) {
			dispatchReplicationStarted(currentReplicationNumber);
			if (config.isIgnoreRunImmediateEmitters()) {
				dispatchSimulationUpdated();
			}
			dispatchReplicationEnded(currentReplicationNumber);
		}
		currentSimulationRun = null;
		onEnded();
		dispatchSimulationEnded();
		running = false;
	}
	
	public void stop() {
		running = false;
		SimulationRun run = currentSimulationRun;
		if(run != null) {
			run.stop();
		}
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
		replicationEndedEmitters.reset();
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
	
	public abstract void replan(SimulationRun original, SimulationRun newOne);
	
	public abstract void onStarted();
	
	public abstract void onEnded();
	
	public abstract void onReplicationStart(int replicationIndex);
	
	public abstract void onReplicationEnd(int replicationIndex);
	
	public boolean isRunning() {
		return running;
	}
}
