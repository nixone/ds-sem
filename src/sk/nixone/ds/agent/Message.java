package sk.nixone.ds.agent;

import OSPABA.MessageForm;

public abstract class Message extends MessageForm {

	protected Message(MessageForm original) {
		super(original);
	}

	public Message(SimulationRun simulation) {
		super(simulation);
	}
}
