package sk.nixone.ds.core;

/**
 * Simple structure used to determine certain process and its duration happening
 * on a certain object in a system.
 * 
 * @author nixone
 *
 */
public class ProcessMarker {
	private boolean started = false;
	private boolean ended = false;
	private double startTime = 0;
	private double endTime = 0;
	
	public void reset() {
		started = ended = false;
		startTime = endTime = 0;
	}
	
	/**
	 * Marks, that the process just started
	 * @param run current simulation run
	 * @throws IllegalStateException when the process was already started before
	 */
	public void started(double time) {
		if(started) {
			throw new IllegalStateException("started==true");
		}
		started = true;
		startTime = time;
	}
	
	protected void moveStartTime(double offset) {
		startTime += offset;
	}
	
	/**
	 * Marks, that the process just ended
	 * @param run current simulation run
	 * @throws IllegalStateException when the process was already ended before
	 */
	public void ended(double time) {
		if(ended) {
			throw new IllegalStateException("ended==true");
		}
		ended = true;
		endTime = time;
	}
	
	/**
	 * Returns the duration. If the process was not started, it will return 0. If the process
	 * was started but not ended, the result of this method can make no sense and should not be used for anything.
	 * 
	 * @return duration
	 */
	public double getDuration() {
		return endTime-startTime;
	}
	
	/**
	 * Determines whether the process did already start (or was finished too)
	 * @return whether the process did happen
	 */
	public boolean didHappen() {
		return started;
	}
	
	/**
	 * Determines whether the process did finish (and start of course)
	 * @return whether the process did finish
	 */
	public boolean didFinish() {
		return ended;
	}
	
	/**
	 * Start time of the process. If not started, the result is undeterminable.
	 * @return start time
	 */
	public double getStartTime() {
		return startTime;
	}
	
	/**
	 * End time of the process. If not ended, the result is undeterminable.
	 * @return
	 */
	public double getEndTime() {
		return endTime;
	}
}
