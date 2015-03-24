package sk.nixone.ds.core.ui;

import javax.swing.JLabel;

public class NumberLabelEmitter extends LabelEmitter<Double> {
	
	private int precision;
	
	public NumberLabelEmitter(JLabel label) {
		this(label, 0);
	}
	
	public NumberLabelEmitter(JLabel label, int precision) {
		super(label);
		this.precision = precision;
		
		reset();
	}
	
	@Override
	public void reset() {
		emit(0.);
	}

	@Override
	public void setToLabel(JLabel label, Double value) {
		if(precision == 0) {
			label.setText(String.format("%d", (int)Math.round(value)));
		} else {
			label.setText(String.format("%."+precision+"f", value));
		}
	}
}
