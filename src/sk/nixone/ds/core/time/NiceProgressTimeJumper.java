package sk.nixone.ds.core.time;

public class NiceProgressTimeJumper implements TimeJumper {

	private double timeFactor;
	
	private double biggestJump;
	
	public NiceProgressTimeJumper() {
		this(1., 0.1);
	}
	
	public NiceProgressTimeJumper(double timeFactor, double biggestJump) {
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
