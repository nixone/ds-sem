package sk.nixone.ds.core.time;

public class AsyncTimeJumper implements TimeJumper {

	private boolean allowed = false;
	
	private Object monitor = new Object();
	
	@Override
	public double jump(double currentTime, double nextEventTime) {
		synchronized(monitor) {
			while(!allowed) {
				try {
					monitor.wait();
				} catch(InterruptedException e) {
					// do nothing, just continue looping
				}
			}
			allowed = false;
		}
		return nextEventTime;
	}
	
	public void allow() {
		synchronized(monitor) {
			allowed = true;
			monitor.notifyAll();
		}
	}
}
