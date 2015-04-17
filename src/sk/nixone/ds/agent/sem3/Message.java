package sk.nixone.ds.agent.sem3;

import OSPABA.MessageForm;
import sk.nixone.ds.agent.Simulation;

public class Message extends MessageForm {
	public Message(Simulation sim)
	{
		super(sim);
	}

	private Message(Message original)
	{
		super(original);
	}
	
	@Override
	public Message createCopy()
	{
		return new Message(this);
	}
}
