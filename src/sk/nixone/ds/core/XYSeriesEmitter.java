package sk.nixone.ds.core;

import javax.swing.SwingUtilities;

import org.jfree.data.xy.XYSeries;

/**
 * Emitter designed to percieve key value pairs of data and add them to XY series that is
 * usually used to display data on a graph. This emitter already ensures safety of modifying data
 * in UI thread.
 * 
 * @author nixone
 *
 */
public class XYSeriesEmitter implements Emitter<Pair<Double, Double>> {

	private XYSeries series;
	
	/**
	 * Creates an emitter on a specific series
	 * 
	 * @param series series of data
	 */
	public XYSeriesEmitter(XYSeries series) {
		this.series = series;
	}
	
	@Override
	public void emit(final Pair<Double, Double> pair) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				series.add(pair.getKey(), pair.getValue());
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
