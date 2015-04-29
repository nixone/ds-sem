package sk.nixone.ds.agent.sem3.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.Emitters;

public class VisualisationPanel extends JPanel {
	
	private Emitters<Double> refresher = new Emitters<Double>();
	private StationsCanvas townCanvas;
	private StationCanvas stationCanvas;
	
	public VisualisationPanel(Model model, StationsLayout layout) {
		townCanvas = new StationsCanvas(model, layout);
		stationCanvas = new StationCanvas();
		
		refresher.add(townCanvas.getRepainter());
		refresher.add(stationCanvas.getRefresher());
		
		townCanvas.setStationCanvas(stationCanvas);
		
		createLayout();
	}
	
	private void createLayout() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weighty = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 5;
		c.gridx = 0;
		add(townCanvas, c);
		c.weightx = 3;
		c.gridx = 1;
		add(stationCanvas, c);
	}
	
	public Emitter<Double> getRefresher() {
		return refresher;
	}
}
