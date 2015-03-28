package sk.nixone.ds.core;

import java.util.HashSet;

public class Emitters<T> implements Emitter<T> {

	private HashSet<Emitter<T>> emitters = new HashSet<Emitter<T>>();
	
	@Override
	public void emit(T value) {
		for(Emitter<T> emitter : emitters) {
			try {
				emitter.emit(value);
			} catch(Throwable cause) {
				cause.printStackTrace();
			}
		}
	}
	
	@Override
	public void reset() {
		for(Emitter<T> emitter : emitters) {
			emitter.reset();
		}
	}
	
	public void add(Emitter<T> emitter) {
		emitters.add(emitter);
	}
}
