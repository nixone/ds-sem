package sk.nixone.ds.core.time;

import java.util.HashSet;

import sk.nixone.ds.core.Emitter;

public abstract class Simulation {
	
	private HashSet<Emitter<Object>> startedEmitters = new HashSet<Emitter<Object>>();
	private HashSet<Emitter<Object>> endedEmitters = new HashSet<Emitter<Object>>();
	private HashSet<Emitter<Integer>> replicationStartedEmitters = new HashSet<Emitter<Integer>>();
	private HashSet<Emitter<Integer>> replicationEndedEmitters = new HashSet<Emitter<Integer>>();
	private HashSet<Emitter<Object>> simulationUpdatedEmitters = new HashSet<Emitter<Object>>();
	
	private SimulationRun currentSimulationRun = null;
	
	private int currentReplicationNumber = -1;

	public void addStartedEmitter(Emitter<Object> emitter) {
		synchronized(startedEmitters) {
			startedEmitters.add(emitter);
		}
	}
	
	public void addEndedEmitter(Emitter<Object> emitter) {
		synchronized(endedEmitters) {
			endedEmitters.add(emitter);
		}
	}
	
	public void addReplicationStartedEmitter(Emitter<Integer> emitter) {
		synchronized(replicationStartedEmitters) {
			replicationStartedEmitters.add(emitter);
		}
	}
	
	public void addReplicationEndedEmitter(Emitter<Integer> emitter) {
		synchronized(replicationEndedEmitters) {
			replicationEndedEmitters.add(emitter);
		}
	}
	
	public void addSimulationUpdaterEmitter(Emitter<Object> emitter) {
		synchronized(simulationUpdatedEmitters) {
			simulationUpdatedEmitters.add(emitter);
		}
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
	
	private <T> HashSet<Emitter<T>> syncCopy(HashSet<Emitter<T>> original) {
		HashSet<Emitter<T>> result = new HashSet<Emitter<T>>();
		synchronized(original) {
			result.addAll(original);
		}
		return result;
	}
	
	private void dispatchSimulationStarted() {
		try {
			for (Emitter<?> emitter : syncCopy(startedEmitters)) {
				emitter.reset();
			}
			for (Emitter<?> emitter : syncCopy(endedEmitters)) {
				emitter.reset();
			}
			for (Emitter<?> emitter : syncCopy(replicationStartedEmitters)) {
				emitter.reset();
			}
			for (Emitter<?> emitter : syncCopy(simulationUpdatedEmitters)) {
				emitter.reset();
			}
			
			for (Emitter<?> emitter : syncCopy(startedEmitters)) {
				emitter.emit(null);
			}
		} catch(Throwable cause) {
			cause.printStackTrace();
		}
	}
	
	private void dispatchReplicationStarted(int replicationIndex) {
		try {
			for (Emitter<?> emitter : syncCopy(simulationUpdatedEmitters)) {
				emitter.reset();
			}
			
			for (Emitter<Integer> emitter : syncCopy(replicationStartedEmitters)) {
				emitter.emit(replicationIndex);
			}
		} catch(Throwable cause) {
			cause.printStackTrace();
		}
	}
	
	protected void dispatchSimulationUpdated() {
		try {
			for (Emitter<Object> emitter : syncCopy(simulationUpdatedEmitters)) {
				emitter.emit(null);
			}
		} catch(Throwable cause) {
			cause.printStackTrace();
		}
	}
	
	private void dispatchReplicationEnded(int replicationIndex) {
		try {
			for (Emitter<Integer> emitter : syncCopy(replicationEndedEmitters)) {
				emitter.emit(replicationIndex);
			}
		} catch(Throwable cause) {
			cause.printStackTrace();
		}
	}
	
	private void dispatchSimulationEnded() {
		try {
			for (Emitter<?> emitter : syncCopy(endedEmitters)) {
				emitter.emit(null);
			}
		} catch(Throwable cause) {
			cause.printStackTrace();
		}
	}
}
