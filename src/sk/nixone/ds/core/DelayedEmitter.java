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
	
	private boolean resetAfter = false;
	
	private boolean resetBefore = false;
	
	private boolean hasValue = false;
	
	public DelayedEmitter(Emitter<T> emitter, long delay) {
		this.emitter = emitter;
		this.delay = delay;
		
		if(schedulingService == null) {
			schedulingService = Executors.newScheduledThreadPool(1);
		}
	}

	@Override
	public synchronized void reset() {
		resetAfter = true;
		trigger();
	}
	
	@Override
	public synchronized void emit(T current) {
		last = current;
		hasValue = true;
		if (resetAfter) {
			resetBefore = true;
			resetAfter = false;
		}
		trigger();
	}
	
	private void trigger() {
		if(!pending) {
			pending = true;
			schedulingService.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						T toEmmit;
						boolean toResetAfter;
						boolean toResetBefore;
						boolean toHasValue;
						
						synchronized(DelayedEmitter.this) {
							toHasValue = hasValue;
							toEmmit = last;
							toResetAfter = resetAfter;
							toResetBefore = resetBefore;
							hasValue = false;
							resetAfter = false;
							resetBefore = false;
							pending = false;
						}
						
						if (toResetBefore) {
							emitter.reset();
						}
						
						if (toHasValue) {
							emitter.emit(toEmmit);
						}
						
						if (toResetAfter) {
							emitter.reset();
						}
					} catch(Throwable t) {
						t.printStackTrace();
					}
				}
			}, delay, TimeUnit.MILLISECONDS);
		}
	}
}
