package sk.nixone.ds.core.time;

/**
 * Time jumper that is as simple as possible. This ensures that the simulation will run like
 * crazy without stopping itself until it ends. It is the faster possible way to run a simulation but
 * one has no control over it.
 *  
 * @author nixone
 *
 */
public class SimpleTimeJumper implements TimeJumper {

    @Override
    public double jump(double currentTime, double nextEventTime) {
		return nextEventTime;
    }
}
