package sk.nixone.ds.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DelayedEmitter<T> implements Emitter<T> {
	
	private static ScheduledExecutorService schedulingService = null;

	private Emitter<T> emitter;
	
	private boolean pending = false;
	
	private long delay;
	
	private T last = null;
	
	public DelayedEmitter(Emitter<T> emitter, long delay) {
		this.emitter = emitter;
		this.delay = delay;
		
		if(schedulingService == null) {
			schedulingService = Executors.newScheduledThreadPool(1);
		}
	}

	@Override
	public void reset() {
		emitter.reset();
	}

	@Override
	public void emit(T current) {
		synchronized(this) {
			last = current;
			
			if(!pending) {
				pending = true;
				schedulingService.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							T toEmmit;
							
							synchronized(DelayedEmitter.this) {
								toEmmit = last;
								pending = false;
							}
	
							emitter.emit(toEmmit);
						} catch(Throwable t) {
							t.printStackTrace();
						}
					}
				}, delay, TimeUnit.MILLISECONDS);
			}
		}
	}
}
