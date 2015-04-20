package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.managers.ModelManager;

public class ModelAgent extends Agent<SimulationRun> {

	public ModelAgent(SimulationRun simulation) {
		super(Components.A_MODEL, simulation, null);
		
		new ModelManager(simulation, this);
	}

}
