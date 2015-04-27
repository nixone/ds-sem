package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.assistants.ExitingPlanner;
import sk.nixone.ds.agent.sem3.managers.ExitingManager;
import sk.nixone.ds.agent.sem3.model.Model;

public class ExitingAgent extends Agent<SimulationRun> {
	
	public ExitingAgent(Model model, SimulationRun simulation, Agent<SimulationRun> parent) {
		super(Components.A_EXITING, simulation, parent);
		
		new ExitingManager(simulation, this);
		new ExitingPlanner(simulation, this);
	}

}
