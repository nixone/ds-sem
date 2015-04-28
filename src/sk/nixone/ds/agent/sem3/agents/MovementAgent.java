package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.assistants.MovementPlanner;
import sk.nixone.ds.agent.sem3.assistants.VehicleInitPlanner;
import sk.nixone.ds.agent.sem3.managers.MovementManager;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Vehicles;

public class MovementAgent extends Agent<SimulationRun> {

	private Model model;
	
	public MovementAgent(Model model, SimulationRun simulation, Agent<SimulationRun> parent) {
		super(Components.A_MOVEMENT, simulation, parent);
		
		this.model = model;
		
		new MovementManager(simulation, this);
		new MovementPlanner(simulation, this);
		new VehicleInitPlanner(simulation, this);
	}

	public Model getModel() {
		return model;
	}
}
