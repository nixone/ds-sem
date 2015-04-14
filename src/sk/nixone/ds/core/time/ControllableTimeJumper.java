package sk.nixone.ds.core.time;

/**
 * Thread safe time jumper which is able to control the simulation by real time flow or by stepping, pausing, etc.
 * 
 * @author nixone
 */
public class ControllableTimeJumper implements TimeJumper {

	private volatile double timeFactor;
	
	private volatile double biggestJump;
	
	private boolean paused = false;
	
	private boolean allowedNextStep = false;
	
	/**
	 * Creates a controllable time jumper with timeFactor=1 and biggestJump=0.1, which
	 * means it will simulate the real time and jump 10 times per second.
	 */
	public ControllableTimeJumper() {
		this(1., 0.1);
	}
	
	/**
	 * Creates a controllable time jumper
	 * @param timeFactor
	 * 		parameter which adjusts the time in linear way. lower time factor, the faster simulation flows in comparison with real time
	 * @param biggestJump
	 * 		resolution of jumps in real time. 0.1 means it will jump 10 times per second (in real time! no in time factored time!)
	 */
	public ControllableTimeJumper(double timeFactor, double biggestJump) {
		this.timeFactor = timeFactor;
		this.biggestJump = biggestJump;
	}
	
	/**
	 * Sets the time factor
	 * @param timeFactor time factor
	 */
	public void setTimeFactor(double timeFactor) {
		this.timeFactor = timeFactor;
	}
	
	/**
	 * Sets the biggest jump in real time
	 * @param biggestJump
	 */
	public void setBiggestJump(double biggestJump) {
		this.biggestJump = biggestJump;
	}
	
	/**
	 * Returns the set time factor
	 * @return set time factor
	 */
	public double getTimeFactor() {
		return timeFactor;
	}
	
	/**
	 * Returns the set biggest jump
	 * @return set biggset jump
	 */
	public double getBiggestJump() {
		return biggestJump;
	}
	
	@Override
	public double jump(double currentTime, double nextEventTime) {
		boolean wasPaused;
		
		synchronized(this) {
			while (paused && !allowedNextStep) {
				try {
					this.wait();
				} catch(InterruptedException e) {
					// do nothing, just continue waiting
				}
			}
			allowedNextStep = false;
			wasPaused = paused;
		}

		if (wasPaused) {
			return nextEventTime;
		}
		
		double nextTime = Math.min(currentTime + biggestJump / timeFactor, nextEventTime);
		
		try {
			Thread.sleep((int)((nextTime - currentTime)*1000*timeFactor));
		} catch(InterruptedException e) {
			// nothing
		}
		
		return nextTime;
	}
	
	/**
	 * Pauses or unpauses the simulation. This can be called from whatever thread possible.
	 * @param paused whether the simulation should be paused or not
	 */
	public synchronized void setPaused(boolean paused) {
		if (this.paused && paused == false) {
			this.notifyAll();
		}
		this.paused = paused;
	}
	
	/**
	 * If the simulation is paused, this will allow the simulation to do next step. If the
	 * simulation is not paused, this method call will do nothing.
	 */
	public synchronized void nextStep() {
		if (this.paused) {
			this.allowedNextStep = true;
			this.notifyAll();
		}
	}
}
