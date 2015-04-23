package sk.nixone.ds.agent.sem3.agents;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.managers.BusMovementManager;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;

public class BusMovementAgent extends Agent<SimulationRun> {

	public BusMovementAgent(Randoms randoms, Model model, SimulationRun simulation, Agent<SimulationRun> parent) {
		super(Components.A_BUS_MOVEMENT, simulation, parent);
		
		new BusMovementManager(simulation, this);
	}

}
