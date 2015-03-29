package sk.nixone.ds.core.ui;

import javax.swing.SwingUtilities;

import sk.nixone.ds.core.Emitter;

public class SwingEmitter<T> implements Emitter<T> {

	private Emitter<T> emitter;
	
	public SwingEmitter(Emitter<T> emitter) {
		this.emitter = emitter;
	}
	
	@Override
	public void reset() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				emitter.reset();
			}
		});
	}

	@Override
	public void emit(final T value) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				emitter.emit(value);				
			}
		});
	}

}
