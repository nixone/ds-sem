package sk.nixone.ds.core;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class PercentageLabelEmitter implements Emitter<Integer, Double> {
	
	private JLabel label;
	
	public PercentageLabelEmitter(JLabel label) {
		this.label = label;
	}
	
	@Override
	public void emit(Integer key, Double value) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				label.setText(String.format("%.6f", value*100)+" %");
			}
		});
	}
	
	@Override
	public void reset() {
		emit(0, 0.);
	}
}
