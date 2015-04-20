package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.assistants.ArrivalPlanner;
import sk.nixone.ds.agent.sem3.managers.SurroundingManager;

public class SurroundingAgent extends Agent<SimulationRun> {
	
	public SurroundingAgent(SimulationRun simulation, Agent<SimulationRun> parent) {
		super(Components.A_SURROUNDING, simulation, parent);
		
		new SurroundingManager(simulation, this);
		new ArrivalPlanner(simulation, this);
	}

}
