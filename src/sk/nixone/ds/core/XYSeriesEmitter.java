package sk.nixone.ds.core;

import javax.swing.SwingUtilities;

import org.jfree.data.xy.XYSeries;

public class XYSeriesEmitter implements Emitter<Integer, Double> {

	private XYSeries series;
	
	public XYSeriesEmitter(XYSeries series) {
		this.series = series;
	}
	
	@Override
	public void emit(final Integer key, final Double value) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				series.add(key, value);
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
