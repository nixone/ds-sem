package sk.nixone.ds.core.time;

public class RealSleepingTimeJumper implements TimeJumper {

    @Override
    public float jump(float currentTime, float nextEventTime) {
	try {
	    Thread.sleep((int)((nextEventTime - currentTime)*1000));
	} catch(InterruptedException e) {
	    // nothing
	}
	return nextEventTime;
    }
}
