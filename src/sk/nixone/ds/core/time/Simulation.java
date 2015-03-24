package sk.nixone.ds.core.time;

import java.util.HashSet;

public abstract class Simulation {
	
	public interface Observer {
		public void onSimulationStarted();
		public void onReplicationStarted(int replicationIndex, SimulationRun run);
		public void onEventPlanned(SimulationRun run, PlannedEvent event);
		public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent);
		public void onVoidStep(SimulationRun run);
		public void onReplicationEnded(int replicationIndex, SimulationRun run);
		public void onSimulationEnded();
	}
	
	public class ObserverAdapter implements Observer {
		@Override
		public void onSimulationStarted() {}
		@Override
		public void onReplicationStarted(int replicationIndex, SimulationRun run) {}
		@Override
		public void onEventPlanned(SimulationRun run, PlannedEvent event) {}
		@Override
		public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent) {}
		@Override
		public void onVoidStep(SimulationRun run) {}
		@Override
		public void onReplicationEnded(int replicationIndex, SimulationRun run) {}
		@Override
		public void onSimulationEnded() {}
	}
	
	private HashSet<Observer> observers;
	
	private SimulationRun currentSimulationRun = null;
	
	public Simulation() {
		observers = new HashSet<>();
	}
	
	public abstract void initializeRun(SimulationRun run);
	
	public void run(int replications) {
		run(new SimpleTimeJumper(), replications);
	}
	
	public void run(TimeJumper jumper, int replications) {
		dispatchSimulationStarted();
		for(int replication=0; replication<replications; replication++) {
			currentSimulationRun = new SimulationRun(this);
			initializeRun(currentSimulationRun);
			
			dispatchReplicaitonStarted(replication, currentSimulationRun);
			currentSimulationRun.run(jumper);
			dispatchReplicationEnded(replication, currentSimulationRun);
			currentSimulationRun = null;
		}
		dispatchSimulationEnded();
	}
	
	public SimulationRun getCurrentSimulationRun() {
		return currentSimulationRun;
	}
	
	public void addObserver(Observer observer) {
		synchronized(observers) {
			observers.add(observer);
		}
	}
	
	public void removeObserver(Observer observer) {
		synchronized(observers) {
			observers.remove(observer);
		}
	}
	
	private HashSet<Observer> copyObservers() {
		HashSet<Observer> observersCopy = new HashSet<>();
		
		synchronized(observers) {
			observersCopy.addAll(observers);
		}
		
		return observersCopy;
	}
	
	private void dispatchSimulationStarted() {
		for(Observer observer : copyObservers()) {
			observer.onSimulationStarted();
		}
	}
	
	private void dispatchReplicaitonStarted(int replicationIndex, SimulationRun run) {
		for(Observer observer : copyObservers()) {
			observer.onReplicationStarted(replicationIndex, run);
		}
	}
	
	private void dispatchReplicationEnded(int replicationIndex, SimulationRun run) {
		for(Observer observer : copyObservers()) {
			observer.onReplicationEnded(replicationIndex, run);
		}
	}
	
	private void dispatchSimulationEnded() {
		for(Observer observer : copyObservers()) {
			observer.onSimulationEnded();
		}
	}
	
	protected void dispatchExecutedEvent(SimulationRun run, PlannedEvent event) {
		for(Observer observer : copyObservers()) {
			observer.onExecutedEvent(run, event);
		}
	}
	
	protected void dispatchVoidStep(SimulationRun run) {
		for(Observer observer : copyObservers()) {
			observer.onVoidStep(run);
		}
	}
	
	protected void dispatchEventPlanned(SimulationRun run, PlannedEvent event) {
		for(Observer observer : copyObservers()) {
			observer.onEventPlanned(run, event);
		}
	}
}
