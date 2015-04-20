package sk.nixone.ds.agent;

import sk.nixone.ds.agent.annotation.HandleMessage;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ContinualAssistant<S extends SimulationRun, A extends Agent<?>> extends OSPABA.ContinualAssistant {

	private HandleMessage.Processor messageProcessor;
	
	public ContinualAssistant(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
		messageProcessor = new HandleMessage.Processor(this);
		for(int code : messageProcessor.getCodes()) {
			getAgent().addOwnMessage(code);
		}
	}

	@Override
	public void processMessage(MessageForm message) {
		messageProcessor.process(message);
	}
	
	public A getAgent() {
		return (A)myAgent();
	}
	
	public S getSimulation() {
		return (S)mySim();
	}
}
