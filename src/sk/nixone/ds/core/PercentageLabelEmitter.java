package sk.nixone.ds.core;

import javax.swing.JLabel;

public class PercentageLabelEmitter implements Emitter<Integer, Double> {
	
	private JLabel label;
	
	public PercentageLabelEmitter(JLabel label) {
		this.label = label;
	}
	
	public void emit(Integer key, Double value) {
		label.setText(String.format("%.6f", value*100)+" %");
	}
}
