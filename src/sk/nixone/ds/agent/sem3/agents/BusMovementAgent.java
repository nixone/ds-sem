package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.assistants.MovementPlanner;
import sk.nixone.ds.agent.sem3.managers.BusMovementManager;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Vehicles;
import sk.nixone.ds.core.Randoms;

public class BusMovementAgent extends Agent<SimulationRun> {

	private Vehicles vehicles;
	
	public BusMovementAgent(Randoms randoms, Model model, SimulationRun simulation, Agent<SimulationRun> parent) {
		super(Components.A_BUS_MOVEMENT, simulation, parent);
		
		vehicles = model.getVehicles();
		
		new BusMovementManager(simulation, this);
		new MovementPlanner(simulation, this);
	}

	public Vehicles getVehicles() {
		return vehicles;
	}
}
