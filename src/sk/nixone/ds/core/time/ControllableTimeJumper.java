package sk.nixone.ds.core.time;

public class ControllableTimeJumper implements TimeJumper {

	private double timeFactor;
	
	private double biggestJump;
	
	private boolean paused = false;
	
	private boolean allowedNextStep = false;
	
	public ControllableTimeJumper() {
		this(1., 0.1);
	}
	
	public ControllableTimeJumper(double timeFactor, double biggestJump) {
		this.timeFactor = timeFactor;
		this.biggestJump = biggestJump / timeFactor;
	}
	
	@Override
	public double jump(double currentTime, double nextEventTime) {
		double nextTime = Math.min(currentTime + biggestJump, nextEventTime);
		
		try {
			Thread.sleep((int)((nextTime - currentTime)*1000*timeFactor));
		} catch(InterruptedException e) {
			// nothing
		}
		return nextTime;
	}

}
