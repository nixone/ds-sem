package sk.nixone.ds.core;

import org.jfree.data.xy.XYSeries;

public class XYSeriesEmitter implements Emitter<Integer, Double> {

	private XYSeries series;
	
	private int dataSizeLimit = 0;
	
	public XYSeriesEmitter(XYSeries series) {
		this.series = series;
	}
	
	@Override
	public void emit(Integer key, Double value) {
		series.add(key, value);
	}
}
