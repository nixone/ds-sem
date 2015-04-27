package sk.nixone.ds.agent.sem3.managers;

import java.util.HashMap;
import java.util.LinkedList;

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

	private HashMap<Line, LinkedList<Vehicle>> vehiclesByLines = new HashMap<Line, LinkedList<Vehicle>>();
	
	public BusMovementManager(SimulationRun simulation, BusMovementAgent agent) {
		super(Components.M_VEHICLE_MOVEMENT, simulation, agent);
	}

	@HandleMessage(code=Messages.INIT)
	public void onInit(Message message) {
		for(Vehicle vehicle : getAgent().getVehicles()) {
			LinkedList<Vehicle> vehicles = vehiclesByLines.get(vehicle.getLine());
			if(vehicles == null) {
				vehicles = new LinkedList<Vehicle>();
				vehiclesByLines.put(vehicle.getLine(), vehicles);
			}
			vehicles.add(vehicle);
		}
		
		for(Line line : vehiclesByLines.keySet()) {
			message = new Message(getSimulation());
			message.setVehicle(vehiclesByLines.get(line).removeFirst());
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
			
			Vehicle nextVehicle = vehiclesByLines.get(message.getVehicle().getLine()).pollFirst();
			if(nextVehicle != null) {
				message = new Message(getSimulation());
				message.setVehicle(nextVehicle);
				message.setStation(nextVehicle.getLine().getFirstStation());
				message.setAddressee(getAgent().findAssistant(Components.VEHICLE_INIT_PLANNER));
				startContinualAssistant(message);
			}
		}
	}
}
