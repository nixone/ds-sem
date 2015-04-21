package sk.nixone.ds.agent.poc;

import sk.nixone.ds.agent.ContinualAssistant;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;

public class Assistant extends ContinualAssistant<SimulationRun, Agent> {

	public Assistant(SimulationRun mySim, Agent myAgent) {
		super(Components.ASSISTANT, mySim, myAgent);
	}

	@HandleMessage(code=Messages.start)
	public void onStart(Message message) {
		hold(10, createMessage());
		hold(30, createMessage());
		hold(20, createMessage());
		hold(15, createMessage());
	}
	
	@HandleMessage(code=Messages.WORK)
	public void onWork(Message message) {
		System.out.println(mySim().currentTime());
	}
	
	private Message createMessage() {
		Message message = new Message(mySim());
		message.setCode(Messages.WORK);
		return message;
	}
}
