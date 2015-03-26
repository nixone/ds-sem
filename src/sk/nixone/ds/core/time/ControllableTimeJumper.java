package sk.nixone.ds.core.time;

public class ControllableTimeJumper implements TimeJumper {

	private volatile double timeFactor;
	
	private volatile double biggestJump;
	
	private boolean paused = false;
	
	private boolean allowedNextStep = false;
	
	public ControllableTimeJumper() {
		this(1., 0.1);
	}
	
	public ControllableTimeJumper(double timeFactor, double biggestJump) {
		this.timeFactor = timeFactor;
		this.biggestJump = biggestJump;
	}
	
	public void setTimeFactor(double timeFactor) {
		this.timeFactor = timeFactor;
	}
	
	public void setBiggestJump(double biggestJump) {
		this.biggestJump = biggestJump;
	}
	
	public double getTimeFactor() {
		return timeFactor;
	}
	
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
	
	public synchronized void setPaused(boolean paused) {
		if (this.paused && paused == false) {
			this.notifyAll();
		}
		this.paused = paused;
	}
	
	public synchronized void nextStep() {
		if (this.paused) {
			this.allowedNextStep = true;
			this.notifyAll();
		}
	}
}
