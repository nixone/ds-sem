package sk.nixone.ds.core.time.ui;

import java.awt.Dimension;

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

import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.Pair;
import sk.nixone.ds.core.Statistic;
import sk.nixone.ds.core.XYSeriesEmitter;
import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.Simulation;
import sk.nixone.ds.core.time.Simulation.Observer;
import sk.nixone.ds.core.time.SimulationRun;
import sk.nixone.ds.core.ui.NumberLabelEmitter;

public class StatisticPanel extends JPanel implements Observer {

	private ChartPanel chartPanel;
	
	private XYSeries series;
	private XYSeriesCollection seriesCollection;
	
	private JLabel meanLabel = new JLabel("Mean:");
	private JLabel meanData = new JLabel();
	
	private Emitter<Double> meanEmitter = new DelayedEmitter<Double>(new NumberLabelEmitter(meanData, 5), 50);
	private Emitter<Pair<Double, Double>> seriesEmitter;

	private Statistic statistic;
	
	public StatisticPanel(Simulation simulation, Statistic statistic, String dataName) {
		this.statistic = statistic;
		
		series = new XYSeries(dataName);
		
		seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(series);
		seriesEmitter = new DelayedEmitter<Pair<Double, Double>>(new XYSeriesEmitter(series), 50);
		
		createComponents(dataName);
		createLayout();
		
		simulation.addObserver(this);
	}
	
	private void createComponents(String dataName) {
		JFreeChart chart = ChartFactory.createXYLineChart(dataName, "Time", "Mean", seriesCollection);
		NumberAxis axis = (NumberAxis)chart.getXYPlot().getRangeAxis();
		axis.setAutoRangeIncludesZero(false);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(800, 600));
		
		meanLabel.setFont(meanLabel.getFont().deriveFont(24f));
		meanData.setFont(meanData.getFont().deriveFont(30f));
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
						)
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(chartPanel)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(meanLabel)
						.addComponent(meanData)
						)
				);
	}

	@Override
	public void onSimulationStarted() {
		meanEmitter.reset();
		seriesEmitter.reset();
	}

	@Override
	public void onReplicationStarted(int replicationIndex, SimulationRun run) {}

	@Override
	public void onEventPlanned(SimulationRun run, PlannedEvent event) {}

	@Override
	public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent) {
		meanEmitter.emit(statistic.getMean());
		seriesEmitter.emit(new Pair<Double, Double>(run.getCurrentSimulationTime(), statistic.getMean()));
	}

	@Override
	public void onVoidStep(SimulationRun run) {}

	@Override
	public void onReplicationEnded(int replicationIndex, SimulationRun run) {}

	@Override
	public void onSimulationEnded() {}
}
