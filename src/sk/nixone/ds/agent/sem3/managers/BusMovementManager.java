package sk.nixone.ds.agent.sem3.managers;

import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.agents.BusMovementAgent;
import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Vehicle;

public class BusMovementManager extends Manager<SimulationRun, BusMovementAgent> {

	public BusMovementManager(SimulationRun simulation, BusMovementAgent agent) {
		super(Components.M_VEHICLE_MOVEMENT, simulation, agent);
	}

	@HandleMessage(code=Messages.INIT)
	public void onInit(Message message) {
		for(Vehicle vehicle : getAgent().getVehicles()) {
			Line line = vehicle.getLine();
			if(line == null) {
				throw new IllegalStateException("Vehicle is not bound to any line!");
			}
			message = new Message(getSimulation());
			message.setVehicle(vehicle);
			message.setStation(line.getFirstStation());
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
