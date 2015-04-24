package sk.nixone.ds.agent.sem3.assistants;

import OSPABA.CommonAgent;
import OSPABA.Simulation;
import sk.nixone.ds.agent.ContinualAssistant;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.agents.BoardingAgent;

public class BoardingPlanner extends ContinualAssistant<SimulationRun, BoardingAgent>{

	public BoardingPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.BOARDING_PLANNER, mySim, myAgent);
	}

	@HandleMessage(code=Messages.start)
	public void onVehicleArrival(Message message) {
		message.setCode(Messages.WAITING_FINISHED);
		notice(message);
	}
	
	@HandleMessage(code=Messages.ENTERING_FINISHED)
	public void onEnteringFinished(Message message) {
		
	}
	
	@HandleMessage(code=Messages.WAITING_FINISHED)
	public void onWaitingFinished(Message message) {
		assistantFinished(message);
	}
}
