package sk.nixone.ds.agent.poc;

import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Manager extends OSPABA.Manager {

	public Manager(Simulation mySim, Agent myAgent) {
		super(Components.MANAGER, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message) {
		switch(message.code()) {
		case Messages.INIT:
			MessageForm msg = new Message(mySim());
			msg.setAddressee(myAgent().findAssistant(Components.ASSISTANT));
			startContinualAssistant(msg);
			break;
		}
	}
}
