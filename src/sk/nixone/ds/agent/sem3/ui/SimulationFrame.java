package sk.nixone.ds.agent.sem3.ui;

import sk.nixone.ds.agent.sem3.Simulation;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Stations;

public class SimulationFrame extends SimulationFrameBase {

	private StationsCanvas stationsCanvas;
	
	public SimulationFrame(Simulation simulation, Model model, StationsLayout stationLayout) {
		super(simulation);

		stationsCanvas = new StationsCanvas(model, stationLayout);
		simulation.getRefreshInvoked().add(stationsCanvas.getRepainter());
		addTab("Town", stationsCanvas);
	}

}
