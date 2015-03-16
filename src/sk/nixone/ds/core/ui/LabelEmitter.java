package sk.nixone.ds.core.ui;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import sk.nixone.ds.core.Emitter;

public abstract class LabelEmitter<TK, TV> implements Emitter<TK, TV> {
	
	private JLabel label;
	
	/**
	 * 
	 * @param label <code>JLabel</code>, v ktorom chceme data zobrazit
	 */
	public LabelEmitter(JLabel label) {
		this.label = label;
	}
	
	@Override
	public void emit(TK key, final TV value) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setToLabel(label, key, value);
			}
		});
	}
	
	public abstract void setToLabel(JLabel label, TK key, TV value);
}
