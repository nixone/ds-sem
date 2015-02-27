package sk.nixone.ds.core;

import java.util.HashSet;

import javax.swing.SwingUtilities;

/**
 * It's important that *UI* methods get called only swing UI thread.
 * 
 * @author nixone
 *
 * @param <T>
 */
public abstract class ValueObserver<T> {
	public abstract void update(int replicationIndex);
	public abstract void updateUI(int replicationIndex);
	
	private HashSet<Emitter<Integer, T>> uiEmitters = new HashSet<Emitter<Integer, T>>();
	
	public void reset() {
		for(Emitter<Integer, T> emitter : uiEmitters) {
			emitter.reset();
		}
	}
	
	public void addUIEmitter(Emitter<Integer, T> emitter) {
		uiEmitters.add(emitter);
	}
	
	public void removeUIEmitter(Emitter<Integer, T> emitter) {
		uiEmitters.remove(emitter);
	}
	
	protected void emitUI(final int replicationIndex, final T value) {
		for(Emitter<Integer, T> emitter : uiEmitters) {
			emitter.emit(replicationIndex, value);
		}
	}
}
