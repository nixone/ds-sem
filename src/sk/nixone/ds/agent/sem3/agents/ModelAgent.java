package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.managers.ModelManager;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;

public class ModelAgent extends Agent<SimulationRun> {

	public ModelAgent(Randoms randoms, Model model, SimulationRun simulation) {
		super(Components.A_MODEL, simulation, null);
		
		new ModelManager(simulation, this);
	}

}
