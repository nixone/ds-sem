package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.assistants.ArrivalPlanner;
import sk.nixone.ds.agent.sem3.managers.SurroundingManager;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Stations;

public class SurroundingAgent extends Agent<SimulationRun> {
	
	private Stations stations;
	
	public SurroundingAgent(Model model, SimulationRun simulation, Agent<SimulationRun> parent) {
		super(Components.A_SURROUNDING, simulation, parent);
		
		this.stations = model.getStations();
		
		new SurroundingManager(simulation, this);
		new ArrivalPlanner(simulation, this);
	}

	public Stations getStations() {
		return stations;
	}
}
