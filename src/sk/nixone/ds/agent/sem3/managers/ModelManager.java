package sk.nixone.ds.agent.sem3.managers;

import OSPABA.MessageForm;
import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.agents.ModelAgent;

public class ModelManager extends Manager<SimulationRun, ModelAgent> {

	public ModelManager(SimulationRun simulation, ModelAgent agent) {
		super(Components.M_MODEL, simulation, agent);
	}
	
	@HandleMessage(code=Messages.INIT)
	public void onStart(Message message) {
		// notify bus movement
		message.setAddressee(getSimulation().findAgent(Components.A_BUS_MOVEMENT));
		notice(message);
		
		MessageForm copy = message.createCopy();
		
		// notify surrounding
		copy.setAddressee(getSimulation().findAgent(Components.A_SURROUNDING));
		notice(copy);
	}
}
