package sk.nixone.ds.agent.poc;

import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Message extends MessageForm {

	public Message(Simulation simulation) {
		super(simulation);
	}
	
	protected Message(Message original) {
		super(original);
	}
	
	@Override
	public Message createCopy() {
		return new Message(this);
	}
}
