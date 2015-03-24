package sk.nixone.ds.core.ui;

import javax.swing.JLabel;

/**
 * Objekt ktory je schopny prijmat percenta zapisane ako double v intervale &lt;0; 1&gt; a
 * zobrazovat ich uzivatelsky privetivo do prideleneho <code>JLabel</code> 
 * 
 * @author nixone
 *
 */
public class PercentageLabelEmitter extends LabelEmitter<Double> {
	/**
	 * 
	 * @param label <code>JLabel</code>, v ktorom chceme data zobrazit
	 */
	public PercentageLabelEmitter(JLabel label) {
		super(label);
	}
	
	@Override
	public void reset() {
		emit(0.);
	}

	@Override
	public void setToLabel(JLabel label, Double value) {
		label.setText(String.format("%.6f", value*100)+" %");
	}
}
