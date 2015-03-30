package sk.nixone.ds.sem2;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import sk.nixone.ds.core.XYSeriesEmitter;

public class CapacityCorelationPanel extends JPanel implements Emitter<Object> {

	private ChartPanel chartPanel;
	
	private XYSeries series;
	private XYSeriesCollection seriesCollection;
	
	private Emitter<Pair<Double, Double>> seriesEmitter;

	private Simulation simulation;
	
	private int latest = 0;
	
	public CapacityCorelationPanel(Simulation simulation) {
		this.simulation = simulation;
		
		series = new XYSeries("Corelation");
		
		seriesCollection = new XYSeriesCollection();
		seriesCollection.addSeries(series);
		seriesEmitter = new DelayedEmitter<Pair<Double, Double>>(new XYSeriesEmitter(series), 50);
		
		createComponents();
		createLayout();
	}
	
	private void createComponents() {
		JFreeChart chart = ChartFactory.createXYLineChart("Corelation between time in system and capacity", "Capacity", "Time in system", seriesCollection);
		NumberAxis axis = (NumberAxis)chart.getXYPlot().getRangeAxis();
		axis.setAutoRangeIncludesZero(false);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(800, 600));
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
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(chartPanel)
				);
	}
	
	@Override
	public void reset() {
	}

	@Override
	public void emit(Object value) {
		if(simulation.people < latest) {
			seriesEmitter.reset();
		}
		latest = simulation.people;
		
		double people = (double)simulation.people;
		double time = (double)simulation.globalStayInSystem.getMean();
		
		if(Double.isNaN(people) || Double.isNaN(time)) {
			return;
		}
		
		seriesEmitter.emit(new Pair<Double, Double>(people, time));
	}
}
