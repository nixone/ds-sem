package sk.nixone.ds.core.time;

import sk.nixone.ds.core.Emitters;

/**
 * Simulation that will run the time simulation runs in itself.
 * 
 * @author nixone
 *
 */
// TODO Should be rewritten as extending of Static simulation
public abstract class Simulation {
	
	private Emitters<Object> startedEmitters = new Emitters<Object>();
	private Emitters<Object> endedEmitters = new Emitters<Object>();
	private Emitters<Object> replicationStartedEmitters = new Emitters<Object>();
	private Emitters<Object> replicationEndedEmitters = new Emitters<Object>();
	private Emitters<Object> simulationUpdatedEmitters = new Emitters<Object>();
	
	private boolean running = false;
	
	private SimulationRun currentSimulationRun = null;
	
	private int currentReplicationNumber = -1;

	/**
	 * Emitters for event of simulation start
	 * @return emitters for event of simulation start
	 */
	public Emitters<Object> getStarted() {
		return startedEmitters;
	}
	
	/**
	 * Emitters for event of simulation end
	 * @return emitters for event of simulation end
	 */
	public Emitters<Object> getEnded() {
		return endedEmitters;
	}
	
	/**
	 * Emitters for event of replication started
	 * @return emitters for event of replication started
	 */
	public Emitters<Object> getReplicationStarted() {
		return replicationStartedEmitters;
	}
	
	/**
	 * Emitters for event of replication ended
	 * @return emitters for event of replication ended
	 */
	public Emitters<Object> getReplicationEnded() {
		return replicationEndedEmitters;
	}
	
	/**
	 * Emitters for event of simulation updated
	 * @return
	 */
	public Emitters<Object> getSimulationUpdated() {
		return simulationUpdatedEmitters;
	}
	
	/**
	 * Runs a simulation with a specified configuration
	 * @param config configuration
	 */
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
			dispatchReplicationStarted(currentReplicationNumber);
			currentSimulationRun.run(config);
			
			onReplicationEnd(currentReplicationNumber);
			dispatchReplicationEnded(currentReplicationNumber);
		}
		currentSimulationRun = null;
		onEnded();
		dispatchSimulationEnded();
		running = false;
	}
	
	/**
	 * Stops the simulation as soon as possible
	 */
	public void stop() {
		running = false;
		SimulationRun run = currentSimulationRun;
		if(run != null) {
			run.stop();
		}
	}
	
	/**
	 * Returns the current replication number that is being processed.
	 * @return current replication number that is being processed
	 */
	public int getCurrentReplicationNumber() {
		return currentReplicationNumber;
	}
	
	/**
	 * Returns the current simulation run being processed.
	 * @return current simulation run being processed.
	 */
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
	
	/**
	 * This is called on the beggining of each replication to replan the events from previous one to this one
	 * @param original previous run, or null when it's the first
	 * @param newOne new simulation run
	 */
	public abstract void replan(SimulationRun original, SimulationRun newOne);
	
	/**
	 * This will be called when the whole simulation is started.
	 */
	public abstract void onStarted();
	
	/**
	 * This will be called when the simulation ends 
	 */
	public abstract void onEnded();
	
	/**
	 * This will be called before each replication, but after <code>onStarted()</code>
	 * @param replicationIndex index of replication being started
	 */
	public abstract void onReplicationStart(int replicationIndex);
	
	/**
	 * This will be called after each replication, but before <code>onEnded()</code>
	 * @param replicationIndex index of replication which did finish
	 */
	public abstract void onReplicationEnd(int replicationIndex);
	
	/**
	 * This will be called from inside of the simulation whenever there was a update in a simulation
	 * @param run
	 */
	public abstract void onUpdated(SimulationRun run);
	
	/**
	 * Determines, whether the simulation is running or not.
	 * @return whether the simulation is running or not
	 */
	public boolean isRunning() {
		return running;
	}
}
