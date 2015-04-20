package sk.nixone.ds.agent.poc;

import OSPABA.CommonAgent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Assistant extends ContinualAssistant {

	public Assistant(Simulation mySim, CommonAgent myAgent) {
		super(Components.ASSISTANT, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message) {
		switch(message.code()) {
		case Messages.start:
			hold(10, createMessage());
			hold(30, createMessage());
			hold(20, createMessage());
			hold(15, createMessage());
			break;
		case Messages.WORK:
			System.out.println(mySim().currentTime());
			break;
		}
	}

	private Message createMessage() {
		Message message = new Message(mySim());
		message.setCode(Messages.WORK);
		return message;
	}
}
