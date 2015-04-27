package sk.nixone.ds.agent.sem3.managers;

import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.agents.BusMovementAgent;
import sk.nixone.ds.agent.sem3.model.Line;

public class BusMovementManager extends Manager<SimulationRun, BusMovementAgent> {

	public BusMovementManager(SimulationRun simulation, BusMovementAgent agent) {
		super(Components.M_VEHICLE_MOVEMENT, simulation, agent);
	}

	@HandleMessage(code=Messages.INIT)
	public void onInit(Message message) {
		for(Line line : getAgent().getModel().getLines()) {
			message = new Message(getSimulation());
			message.setLine(line);
			message.setAddressee(getAgent().findAssistant(Components.VEHICLE_INIT_PLANNER));
			startContinualAssistant(message);
		}
	}
	
	@HandleMessage(code=Messages.VEHICLE_FROM_STATION)
	public void onVehicleFromStation(Message message) {
		message.setAddressee(getAgent().findAssistant(Components.MOVEMENT_PLANNER));
		startContinualAssistant(message);
	}
	
	@HandleMessage(code=Messages.finish)
	public void onAssistantFinished(Message message) {
		if(message.sender().id() == Components.MOVEMENT_PLANNER) {
			
			message.setCode(Messages.VEHICLE_AT_STATION);
			message.setAddressee(getSimulation().findAgent(Components.A_MODEL));
			notice(message);
			
		} else if(message.sender().id() == Components.VEHICLE_INIT_PLANNER) {
			
			message = message.createCopy();
			message.setCode(Messages.VEHICLE_AT_STATION);
			message.getVehicle().setCurrentStation(message.getStation());
			message.setAddressee(getSimulation().findAgent(Components.A_MODEL));
			notice(message);

		}
	}
}
