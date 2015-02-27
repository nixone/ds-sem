package sk.nixone.ds.sem1.ui;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import sk.nixone.ds.core.PercentageLabelEmitter;
import sk.nixone.ds.core.ProbabilityObserver;
import sk.nixone.ds.core.XYSeriesEmitter;

public class TabContent extends Container {
	
	private ChartPanel chartPanel;
	
	private XYSeries series;
	private XYSeriesCollection seriesCollection;
	
	private JLabel legendLabel = new JLabel("Probability:");
	private JLabel dataLabel = new JLabel("0.000000 %");
	
	private PercentageLabelEmitter labelEmitter = new PercentageLabelEmitter(dataLabel);
	private XYSeriesEmitter seriesEmitter;
	
	public TabContent(String dataName, ProbabilityObserver valueObserver) {
		series = new XYSeries(dataName);
		
		seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(series);
		seriesEmitter = new XYSeriesEmitter(series);
		
		createComponents(dataName);
		createLayout();
		
		valueObserver.addUIEmitter(labelEmitter);
		valueObserver.addUIEmitter(seriesEmitter);
	}
	
	public void setMaximumDataPoints(int maximumDataPoints) {
		series.setMaximumItemCount(maximumDataPoints);
	}
	
	private void createComponents(String dataName) {
		JFreeChart chart = ChartFactory.createXYLineChart(dataName, "Replications", "Probability", seriesCollection);
		NumberAxis axis = (NumberAxis)chart.getXYPlot().getRangeAxis();
		axis.setAutoRangeIncludesZero(false);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(800, 600));
		
		legendLabel.setFont(legendLabel.getFont().deriveFont(24f));
		dataLabel.setFont(dataLabel.getFont().deriveFont(30f));
	}
	
	private void createLayout() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(chartPanel)
				.addGroup(layout.createSequentialGroup()
						.addComponent(legendLabel)
						.addComponent(dataLabel)
						)
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(chartPanel)
				.addGroup(layout.createParallelGroup()
						.addComponent(legendLabel)
						.addComponent(dataLabel)
						)
				);
	}
}
