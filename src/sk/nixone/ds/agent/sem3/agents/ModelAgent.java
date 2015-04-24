package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.managers.ModelManager;
import sk.nixone.ds.agent.sem3.model.Model;

public class ModelAgent extends Agent<SimulationRun> {

	public ModelAgent(Model model, SimulationRun simulation) {
		super(Components.A_MODEL, simulation, null);
		
		new ModelManager(simulation, this);
	}

}
