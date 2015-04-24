package sk.nixone.ds.core.time;

/**
 * Simple structure used to determine certain process and its duration happening
 * on a certain object in a system.
 * 
 * @author nixone
 *
 */
public class ProcessMarker extends sk.nixone.ds.core.ProcessMarker {
	
	/**
	 * Marks, that the process just started
	 * @param run current simulation run
	 * @throws IllegalStateException when the process was already started before
	 */
	public void started(SimulationRun run) {
		run.ongoingMarkers.add(this);
		super.started(run.getCurrentSimulationTime());
	}
	
	protected void moveStartTime(double offset) {
		super.moveStartTime(offset);
	}
	
	/**
	 * Marks, that the process just ended
	 * @param run current simulation run
	 * @throws IllegalStateException when the process was already ended before
	 */
	public void ended(SimulationRun run) {
		run.ongoingMarkers.remove(this);
		super.ended(run.getCurrentSimulationTime());
	}
}
