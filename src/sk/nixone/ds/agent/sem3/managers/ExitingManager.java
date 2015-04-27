package sk.nixone.ds.agent.sem3.managers;

import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.agents.ExitingAgent;

public class ExitingManager extends Manager<SimulationRun, ExitingAgent> {

	public ExitingManager(sk.nixone.ds.agent.SimulationRun simulation, ExitingAgent agent) {
		super(Components.M_EXITING, simulation, agent);
	}

	@HandleMessage(code=Messages.VEHICLE_AT_STATION)
	public void onVehicleAtStation(Message message) {
		message.setAddressee(getAgent().findAssistant(Components.EXITING_PLANNER));
		startContinualAssistant(message);
	}
	
	@HandleMessage(code=Messages.finish)
	public void onExitingFinished(Message message) {
		message.setCode(Messages.VEHICLE_FROM_STATION);
		message.setAddressee(getSimulation().findAgent(Components.A_MODEL));
		notice(message);
	}
}
