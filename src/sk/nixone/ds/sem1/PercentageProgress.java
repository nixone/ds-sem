package sk.nixone.ds.sem1;

import java.awt.Label;

import javax.swing.SwingUtilities;

import org.jfree.data.xy.XYSeries;

import sk.nixone.ds.core.EventProbabilityObserver;

public class PercentageProgress implements ProgressDisplay
{
	private Label label;
	private EventProbabilityObserver<?> observer;
	private XYSeries series;
	
	public PercentageProgress(Label label, EventProbabilityObserver<?> observer, XYSeries series) {
		this.label = label;
		this.observer = observer;
		this.series = series;
	}
	
	public void update(int sampleCount)
	{
		final double value = observer.getProbability();
		series.add(sampleCount, value);
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				label.setText(String.format("%.10f", value*100)+" %");
			}
		});
	}
}
