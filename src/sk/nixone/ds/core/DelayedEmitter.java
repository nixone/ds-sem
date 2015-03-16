package sk.nixone.ds.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DelayedEmitter<K, V> implements Emitter<K, V> {
	
	private static ScheduledExecutorService schedulingService = null;

	private Emitter<K, V> emitter;
	private ScheduledExecutorService service;
	
	private boolean pending = false;
	
	private long delay;
	
	private K lastKey = null;
	private V lastValue = null;
	
	public DelayedEmitter(Emitter<K, V> emitter, long delay) {
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
	public void emit(K key, V value) {
		synchronized(this) {
			lastKey = key;
			lastValue = value;
			
			if(!pending) {
				pending = true;
				
				schedulingService.schedule(new Runnable() {
					@Override
					public void run() {
						K keyToEmmit;
						V valueToEmmit;
						
						synchronized(DelayedEmitter.this) {
							keyToEmmit = lastKey;
							valueToEmmit = lastValue;
							pending = false;
						}
						
						emitter.emit(keyToEmmit, valueToEmmit);
					}
				}, delay, TimeUnit.MILLISECONDS);
			}
		}
	}
}
