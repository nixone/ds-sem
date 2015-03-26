
package sk.nixone.ds.core.ui;

import javax.swing.JLabel;

import sk.nixone.util.TimeUtil;

/**
 *
 * @author olesnanik2
 */
public class TimeLabelEmitter extends LabelEmitter<Double> {

	public TimeLabelEmitter(JLabel label) {
		super(label);
	}

	@Override
	public void reset() {
		emit(0.);
	}

	@Override
	public void setToLabel(JLabel label, Double value) {
		label.setText(TimeUtil.toString(Math.round(value)));
	}
}
