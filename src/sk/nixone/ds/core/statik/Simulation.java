package sk.nixone.ds.core.statik;


import sk.nixone.ds.core.Emitters;

/**
 * Zaklad statickej simulacie, ktora sa od ostatnych odlisuje hlavne absenciou casu pocas priebehu simulacie.
 * Pred simulovanim je mozne statickej simulacii pridelit roznych pozorovatelov stavu simulacie, ktori su automaticky
 * aktualizovany podla momentalneho priebehu simulacie.
 * 
 * @author nixone
 */
public abstract class Simulation {

	private Emitters<Object> startedEmitters = new Emitters<Object>();
	private Emitters<Object> endedEmitters = new Emitters<Object>();
	private Emitters<Object> replicationStartedEmitters = new Emitters<Object>();
	private Emitters<Object> replicationEndedEmitters = new Emitters<Object>();
	
	private boolean running = false;
	
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
	 * Runs a simulation with a specified configuration
	 * @param config configuration
	 */
	public void run(int replications) {
		running = true;
		dispatchSimulationStarted();
		onStarted();
		for(currentReplicationNumber=0; running==true && currentReplicationNumber<replications; currentReplicationNumber++) {
			onReplicationStart(currentReplicationNumber);
			dispatchReplicationStarted(currentReplicationNumber);
			runReplication();
			onReplicationEnd(currentReplicationNumber);
			dispatchReplicationEnded(currentReplicationNumber);
		}
		onEnded();
		dispatchSimulationEnded();
		running = false;
	}
	
	/**
	 * Stops the simulation as soon as possible
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Returns the current replication number that is being processed.
	 * @return current replication number that is being processed
	 */
	public int getCurrentReplicationNumber() {
		return currentReplicationNumber;
	}
	
	protected void dispatchSimulationStarted() {
		startedEmitters.reset();
		endedEmitters.reset();
		replicationStartedEmitters.reset();
		replicationEndedEmitters.reset();
		startedEmitters.emit(null);
	}
	
	protected void dispatchReplicationStarted(int replicationIndex) {
		replicationStartedEmitters.emit(replicationIndex);
	}
	
	protected void dispatchReplicationEnded(int replicationIndex) {
		replicationEndedEmitters.emit(replicationIndex);
	}
	
	protected void dispatchSimulationEnded() {
		endedEmitters.emit(null);
	}
	
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
	 * Determines, whether the simulation is running or not.
	 * @return whether the simulation is running or not
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Metoda zodpovedna za vykonanie jednej konkretnej replikacie simulacneho modelu
	 */
	public abstract void runReplication();
}
