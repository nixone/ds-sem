package sk.nixone.ds.agent;

import sk.nixone.ds.agent.annotation.HandleMessage;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Process extends OSPABA.Process {

	private HandleMessage.Processor messageProcessor;
	
	public Process(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
		messageProcessor = new HandleMessage.Processor(this);
	}

	@Override
	public void processMessage(MessageForm message) {
		messageProcessor.process(message);
	}

}
