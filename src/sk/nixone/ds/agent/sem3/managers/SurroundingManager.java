package sk.nixone.ds.agent.sem3.managers;

import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.agents.SurroundingAgent;

public class SurroundingManager extends Manager<SimulationRun, SurroundingAgent> {

	public SurroundingManager(SimulationRun simulation, SurroundingAgent agent) {
		super(Components.M_SURROUNDING, simulation, agent);
	}
	
	@HandleMessage(code=Messages.INIT)
	public void onInit(Message message) {
		message = new Message(getSimulation());
		message.setAddressee(getAgent().findAssistant(Components.ARRIVAL_PLANNER));
		startContinualAssistant(message);
	}
	
	@HandleMessage(code=Messages.finish)
	public void onAssistantFinished(Message message) {
		message = message.createCopy();
		message.setCode(Messages.NEW_TRAVELER);
		message.setAddressee(getSimulation().findAgent(Components.A_MODEL));
		notice(message);
	}
}
