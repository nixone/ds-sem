package sk.nixone.ds.agent;

import sk.nixone.ds.agent.annotation.HandleMessage;
import OSPABA.MessageForm;

public class Manager<A extends Agent<?>> extends OSPABA.Manager {

	private HandleMessage.Processor messageProcessor;
	
	public Manager(int id, SimulationRun simulation, A agent) {
		super(id, simulation, agent);
		messageProcessor = new HandleMessage.Processor(this);
		for(int message : messageProcessor.getCodes()) {
			agent.addOwnMessage(message);
		}
	}

	@Override
	public void processMessage(MessageForm message) {
		messageProcessor.process(message);
	}
	
	public A getAgent() {
		return (A)myAgent();
	}
}
