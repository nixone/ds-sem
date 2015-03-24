package sk.nixone.ds.core;

import javax.swing.SwingUtilities;

import org.jfree.data.xy.XYSeries;

/**
 * Objekt pre sledovanie priebehu simulacie ktory medzivysledky typu <code>double</code> zobrazuje
 * do triedy <code>XYSeries</code>, ktora sa obycajne pouziva na zobrazovanie pozadovanych dat do grafu.
 * 
 * @author nixone
 *
 */
public class XYSeriesEmitter implements Emitter<Pair<Double, Double>> {

	private XYSeries series;
	
	public XYSeriesEmitter(XYSeries series) {
		this.series = series;
	}
	
	@Override
	public void emit(final Pair<Double, Double> pair) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				series.add(pair.key, pair.value);
			}
		});
	}
	
	@Override
	public void reset() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				series.clear();
			}
		});
	}
}
