package sk.nixone.ds.agent.sem3.ui;

import sk.nixone.ds.agent.sem3.Simulation;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Stations;
import sk.nixone.ds.core.ui.property.PropertyPanel;

public class SimulationFrame extends SimulationFrameBase {
	
	public SimulationFrame(Simulation simulation, Model model, StationsLayout stationLayout) {
		super(simulation);

		VisualisationPanel visualPanel = new VisualisationPanel(model, stationLayout);
		simulation.getRefreshInvoked().add(visualPanel.getRefresher());
		addTab("Visual", visualPanel);
		
		PropertyPanel configPanel = new PropertyPanel(model.getProperties());
		addTab("Config", configPanel);
	}

}
