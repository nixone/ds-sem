package sk.nixone.ds.core.ui;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import sk.nixone.ds.core.Emitter;

public abstract class LabelEmitter<T> implements Emitter<T> {
	
	private JLabel label;
	
	/**
	 * 
	 * @param label <code>JLabel</code>, v ktorom chceme data zobrazit
	 */
	public LabelEmitter(JLabel label) {
		this.label = label;
		reset();
	}
	
	@Override
	public void emit(final T value) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setToLabel(label, value);
			}
		});
	}
	
	public abstract void setToLabel(JLabel label, T value);
}
