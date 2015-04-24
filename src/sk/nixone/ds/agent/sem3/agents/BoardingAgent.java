package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.assistants.BoardingPlanner;
import sk.nixone.ds.agent.sem3.managers.BoardingManager;
import sk.nixone.ds.agent.sem3.model.Model;

public class BoardingAgent extends Agent<SimulationRun> {
	
	public BoardingAgent(Model model, SimulationRun simulation, Agent<SimulationRun> parent) {
		super(Components.A_BOARDING, simulation, parent);
		
		new BoardingManager(simulation, this);
		new BoardingPlanner(simulation, this);
	}

}
