package sk.nixone.ds.core.time;

public class SimpleTimeJumper implements TimeJumper {

    @Override
    public double jump(double currentTime, double nextEventTime) {
		return nextEventTime;
    }
}
