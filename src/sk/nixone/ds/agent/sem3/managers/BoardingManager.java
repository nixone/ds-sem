package sk.nixone.ds.agent.sem3.managers;

import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.agents.BoardingAgent;

public class BoardingManager extends Manager<SimulationRun, BoardingAgent>{

	public BoardingManager(sk.nixone.ds.agent.SimulationRun simulation, BoardingAgent agent) {
		super(Components.M_BOARDING, simulation, agent);
	}
	
	@HandleMessage(code=Messages.VEHICLE_AT_STATION)
	public void onVehicleAtStation(Message message) {
		message.setAddressee(getAgent().findAssistant(Components.BOARDING_PLANNER));
		startContinualAssistant(message);
	}
	
	@HandleMessage(code=Messages.NEW_TRAVELER)
	public void onNewTraveler(Message message) {
		message.setAddressee(getAgent().findAssistant(Components.BOARDING_PLANNER));
		startContinualAssistant(message);
	}
	
	@HandleMessage(code=Messages.finish)
	public void onBoardingFinished(Message message) {
		message.setCode(Messages.VEHICLE_FROM_STATION);
		message.setAddressee(getSimulation().findAgent(Components.A_MODEL));
		notice(message);
	}
}
