package sk.nixone.ds.agent.sem3.managers;

import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.agents.BusMovementAgent;

public class BusMovementManager extends Manager<SimulationRun, BusMovementAgent> {

	public BusMovementManager(SimulationRun simulation, BusMovementAgent agent) {
		super(Components.M_BUS_MOVEMENT, simulation, agent);
	}

	@HandleMessage(code=Messages.INIT)
	public void onInit(Message message) {
	}
}
