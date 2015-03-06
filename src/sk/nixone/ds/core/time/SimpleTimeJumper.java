package sk.nixone.ds.core.time;

public class SimpleTimeJumper implements TimeJumper {

    @Override
    public float jump(float currentTime, float nextEventTime) {
	return nextEventTime;
    }
}
