package sk.nixone.ds.agent.sem3.ui;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

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
		
		townCanvas.setPreferredSize(new Dimension(800, 600));
		stationCanvas.setPreferredSize(new Dimension(600, 600));
		
		createLayout();
	}
	
	private void createLayout() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(townCanvas)
				.addComponent(stationCanvas)
				);
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(townCanvas)
				.addComponent(stationCanvas)
				);
	}
	
	public Emitter<Double> getRefresher() {
		return refresher;
	}
}
