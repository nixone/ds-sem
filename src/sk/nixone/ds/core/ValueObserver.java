package sk.nixone.ds.core;

import java.util.HashSet;

public abstract class ValueObserver<T> {
	public abstract void update(int replicationIndex);
	public abstract void updateUI(int replicationIndex);
	public abstract void reset();
	
	private HashSet<Emitter<Integer, T>> uiEmitters = new HashSet<Emitter<Integer, T>>();
	
	public void addUIEmitter(Emitter<Integer, T> emitter) {
		uiEmitters.add(emitter);
	}
	
	public void removeUIEmitter(Emitter<Integer, T> emitter) {
		uiEmitters.remove(emitter);
	}
	
	protected void emitUI(int replicationIndex, T value) {
		for(Emitter<Integer, T> emitter : uiEmitters) {
			emitter.emit(replicationIndex, value);
		}
	}
}
