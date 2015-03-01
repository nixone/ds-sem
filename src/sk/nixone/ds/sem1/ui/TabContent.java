package sk.nixone.ds.sem1.ui;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import sk.nixone.ds.core.PercentageLabelEmitter;
import sk.nixone.ds.core.ProbabilityObserver;
import sk.nixone.ds.core.XYSeriesEmitter;

/**
 * Zobrazovaci panel zodpovedny za zobrazenie jednej sledovanej hodnoty v priebehu simulacie (napriklad pritomnost
 * udalosti "Na konci hry ostali vsetci hraci mrtvi".
 * 
 * @author nixone
 *
 */
public class TabContent extends Container {
	
	private ChartPanel chartPanel;
	
	private XYSeries series;
	private XYSeriesCollection seriesCollection;
	
	private JLabel legendLabel = new JLabel("Probability:");
	private JLabel dataLabel = new JLabel("unknown %");
	
	private PercentageLabelEmitter labelEmitter = new PercentageLabelEmitter(dataLabel);
	private XYSeriesEmitter seriesEmitter;
	
	/**
	 * Vytvori panel v zavislosti od daneho pozorovatela v ramci simulacie.
	 * 
	 * @param dataName popisny nazov sledovanej udalosti pre uzivatela
	 * @param valueObserver pozorovatel, na zaklade ktoreho budu zobrazovanie data uzivatelovi
	 */
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
	
	/**
	 * Nastavi maximalny pocet dat, ktore su historicky uzivatelovi prezentovane
	 * @param maximumDataPoints pocet dat
	 */
	public void setMaximumDataPoints(int maximumDataPoints) {
		series.setMaximumItemCount(maximumDataPoints);
	}
	
	/**
	 * Vytvori zakladne komponenty panelu a nastavi ich
	 * @param dataName popisny nazov sledovanej udalosti pre uzivatela
	 */
	private void createComponents(String dataName) {
		JFreeChart chart = ChartFactory.createXYLineChart(dataName, "Replications", "Probability", seriesCollection);
		NumberAxis axis = (NumberAxis)chart.getXYPlot().getRangeAxis();
		axis.setAutoRangeIncludesZero(false);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(800, 600));
		
		legendLabel.setFont(legendLabel.getFont().deriveFont(24f));
		dataLabel.setFont(dataLabel.getFont().deriveFont(30f));
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
						.addComponent(legendLabel)
						.addComponent(dataLabel)
						)
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(chartPanel)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(legendLabel)
						.addComponent(dataLabel)
						)
				);
	}
}
