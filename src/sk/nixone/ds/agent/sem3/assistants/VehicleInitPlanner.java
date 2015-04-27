package sk.nixone.ds.agent.sem3.assistants;

import OSPABA.CommonAgent;
import OSPABA.Simulation;
import sk.nixone.ds.agent.ContinualAssistant;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.agents.BusMovementAgent;

public class VehicleInitPlanner extends ContinualAssistant<SimulationRun, BusMovementAgent>{

	public VehicleInitPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.VEHICLE_INIT_PLANNER, mySim, myAgent);
	}
	
	@HandleMessage(code=Messages.start)
	public void onStart(Message message) {
		message = message.createCopy();
		message.setCode(Messages.VEHICLE_INIT_FINISHED);
		hold(message.getVehicle().getLine().getInitWaitingTime(), message);
	}
	
	@HandleMessage(code=Messages.VEHICLE_INIT_FINISHED)
	public void onEnd(Message message) {
		assistantFinished(message);
	}
}
