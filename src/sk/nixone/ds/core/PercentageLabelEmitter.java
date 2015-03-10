package sk.nixone.ds.core;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Objekt ktory je schopny prijmat percenta zapisane ako double v intervale &lt;0; 1&gt; a
 * zobrazovat ich uzivatelsky privetivo do prideleneho <code>JLabel</code> 
 * 
 * @author nixone
 *
 */
public class PercentageLabelEmitter implements Emitter<Integer, Double> {
	
	private JLabel label;
	
	/**
	 * 
	 * @param label <code>JLabel</code>, v ktorom chceme data zobrazit
	 */
	public PercentageLabelEmitter(JLabel label) {
		this.label = label;
	}
	
	@Override
	public void emit(Integer key, final Double value) {
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
