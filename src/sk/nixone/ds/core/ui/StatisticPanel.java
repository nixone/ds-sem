package sk.nixone.ds.core.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import sk.nixone.ds.core.ConfidenceInterval;
import sk.nixone.ds.core.ConfidenceStatistic;
import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.Pair;
import sk.nixone.ds.core.Statistic;
import sk.nixone.ds.core.XYSeriesEmitter;
import sk.nixone.ds.core.time.Simulation;

public class StatisticPanel extends JPanel implements Emitter<Object> {

	private ChartPanel chartPanel;
	
	private XYSeries series;
	private XYSeriesCollection seriesCollection;
	
	private JLabel meanLabel = new JLabel("Mean:");
	private JLabel meanData = new JLabel();
	
	private JLabel confidenceLabel = new JLabel("Confidence interval:");
	private JLabel confidenceSuffix = new JLabel(" < ");
	private JLabel confidenceBottom = new JLabel("N/A");
	private JLabel confidenceSeparator = new JLabel(" ; ");
	private JLabel confidenceTop = new JLabel("N/A");
	private JLabel confidenceAppendix = new JLabel(" > ");
	
	private Emitter<Double> meanEmitter = new DelayedEmitter<Double>(createEmitter(meanData), 15);
	private Emitter<Pair<Double, Double>> seriesEmitter;
	private Emitter<Double> bottomCIEmitter = new DelayedEmitter<Double>(createEmitter(confidenceBottom), 15);
	private Emitter<Double> topCIEmitter = new DelayedEmitter<Double>(createEmitter(confidenceTop), 15);
	
	private Statistic statistic;
	private Simulation simulation;
	
	private int key = 0;
	
	public StatisticPanel(Simulation simulation, Statistic statistic, String dataName) {
		this.statistic = statistic;
		this.simulation = simulation;
		
		series = new XYSeries(dataName);
		
		seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(series);
		seriesEmitter = new DelayedEmitter<Pair<Double, Double>>(new XYSeriesEmitter(series), 50);
		
		createComponents(dataName);
		createLayout();
	}
	
	private void createComponents(String dataName) {
		if(!(statistic instanceof ConfidenceStatistic)) {
			confidenceLabel.setVisible(false);
			confidenceSuffix.setVisible(false);
			confidenceBottom.setVisible(false);
			confidenceSeparator.setVisible(false);
			confidenceTop.setVisible(false);
			confidenceAppendix.setVisible(false);
		}
		confidenceLabel.setFont(confidenceLabel.getFont().deriveFont(24f));
		confidenceSuffix.setFont(confidenceSuffix.getFont().deriveFont(24f));
		confidenceBottom.setFont(confidenceBottom.getFont().deriveFont(24f));
		confidenceSeparator.setFont(confidenceSeparator.getFont().deriveFont(24f));
		confidenceTop.setFont(confidenceTop.getFont().deriveFont(24f));
		confidenceAppendix.setFont(confidenceAppendix.getFont().deriveFont(24f));
		confidenceBottom.setFont(confidenceBottom.getFont().deriveFont(Font.BOLD));
		confidenceTop.setFont(confidenceTop.getFont().deriveFont(Font.BOLD));
		
		JFreeChart chart = ChartFactory.createXYLineChart(dataName, "Time", "Mean", seriesCollection);
		NumberAxis axis = (NumberAxis)chart.getXYPlot().getRangeAxis();
		axis.setAutoRangeIncludesZero(false);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(800, 600));
		
		meanLabel.setFont(meanLabel.getFont().deriveFont(24f));
		meanData.setFont(meanData.getFont().deriveFont(30f).deriveFont(Font.BOLD));
	}

	/**
	 * Vytvori a nastavi rozlozenie prvkov uzivatelskeho rozhrania na paneli
	 */
	private void createLayout() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(chartPanel)
				.addGroup(layout.createSequentialGroup()
						.addComponent(meanLabel)
						.addComponent(meanData)
						.addComponent(confidenceLabel)
						.addComponent(confidenceSuffix)
						.addComponent(confidenceBottom)
						.addComponent(confidenceSeparator)
						.addComponent(confidenceTop)
						.addComponent(confidenceAppendix)
						)
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(chartPanel)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(meanLabel)
						.addComponent(meanData)
						.addComponent(confidenceLabel)
						.addComponent(confidenceSuffix)
						.addComponent(confidenceBottom)
						.addComponent(confidenceSeparator)
						.addComponent(confidenceTop)
						.addComponent(confidenceAppendix)
						)
				);
	}
	
	@Override
	public void reset() {
		key = 0;
		meanEmitter.reset();
		seriesEmitter.reset();
		bottomCIEmitter.reset();
		topCIEmitter.reset();
	}

	@Override
	public void emit(Object value) {
		meanEmitter.emit(statistic.getMean());
		seriesEmitter.emit(new Pair<Double, Double>((double)key++, statistic.getMean()));
		if(statistic instanceof ConfidenceStatistic) {
			ConfidenceInterval interval = ConfidenceInterval.count(ConfidenceInterval.ALPHA_95, (ConfidenceStatistic)statistic);
			bottomCIEmitter.emit(interval.getBottomBound());
			topCIEmitter.emit(interval.getTopBound());
		}
	}
	
	public Emitter<Double> createEmitter(JLabel label) {
		return new NumberLabelEmitter(label, 3);
	}
}
