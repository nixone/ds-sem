package sk.nixone.ds.core.time;

import java.util.HashSet;

import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.time.Simulation.Observer;

public class ObserverEmitterAdapter implements Observer {
	
	private HashSet<Emitter<Object>> startedEmitters = new HashSet<Emitter<Object>>();
	private HashSet<Emitter<Object>> endedEmitters = new HashSet<Emitter<Object>>();
	private HashSet<Emitter<Integer>> replicationStartedEmitters = new HashSet<Emitter<Integer>>();
	private HashSet<Emitter<Integer>> replicationEndedEmitters = new HashSet<Emitter<Integer>>();
	private HashSet<Emitter<Object>> simulationUpdatedEmitters = new HashSet<Emitter<Object>>();
	
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
	
	private <T> HashSet<Emitter<T>> syncCopy(HashSet<Emitter<T>> original) {
		HashSet<Emitter<T>> result = new HashSet<Emitter<T>>();
		synchronized(original) {
			result.addAll(original);
		}
		return result;
	}
	
	@Override
	public void onSimulationStarted() {
		for (Emitter<?> emitter : syncCopy(startedEmitters)) {
			emitter.emit(null);
		}
	}

	@Override
	public void onReplicationStarted(int replicationIndex, SimulationRun run) {
		for (Emitter<Integer> emitter : syncCopy(replicationStartedEmitters)) {
			emitter.emit(replicationIndex);
		}
	}

	@Override
	public void onEventPlanned(SimulationRun run, PlannedEvent event) {
		// nothing
	}

	@Override
	public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent) {
		onVoidStep(run);
	}

	@Override
	public void onVoidStep(SimulationRun run) {
		for (Emitter<Object> emitter : syncCopy(simulationUpdatedEmitters)) {
			emitter.emit(null);
		}
	}

	@Override
	public void onReplicationEnded(int replicationIndex, SimulationRun run) {
		for (Emitter<Integer> emitter : syncCopy(replicationEndedEmitters)) {
			emitter.emit(replicationIndex);
		}
	}

	@Override
	public void onSimulationEnded() {
		for (Emitter<?> emitter : syncCopy(endedEmitters)) {
			emitter.emit(null);
		}
	}
}
