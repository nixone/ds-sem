package sk.nixone.ds.agent.poc;

import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;
import OSPABA.MessageForm;

public class Manager extends sk.nixone.ds.agent.Manager<SimulationRun, Agent> {

	public Manager(SimulationRun mySim, Agent myAgent) {
		super(Components.MANAGER, mySim, myAgent);
	}

	@HandleMessage(code=Messages.INIT)
	public void onInit(Message message) {
		MessageForm msg = new Message(mySim());
		msg.setAddressee(myAgent().findAssistant(Components.ASSISTANT));
		startContinualAssistant(msg);
	}
}
