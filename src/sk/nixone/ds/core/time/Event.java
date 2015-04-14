package sk.nixone.ds.core.time;

/**
 * Anything that can be executed with respect to the simulation run.
 * 
 * @author nixone
 *
 */
public interface Event {
   
	/**
	 * Execute this, whatever it should be.
	 * @param run context in which it is executed
	 */
    public void execute(SimulationRun run);
}
