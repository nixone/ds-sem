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
import sk.nixone.ds.agent.sem3.model.Station;
import sk.nixone.ds.agent.sem3.model.Vehicle;

public class MovementPlanner extends ContinualAssistant<SimulationRun, BusMovementAgent> {

	public MovementPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.MOVEMENT_PLANNER, mySim, myAgent);
	}

	@HandleMessage(code=Messages.start)
	public void onStart(Message message) {
		Vehicle vehicle = message.getVehicle();
		Station from = vehicle.getCurrentStation();
		Station to = vehicle.getLine().getNextStation(from, true);
		double travelTime = vehicle.getLine().getNextStationTime(from);
		double arrivalTime = getSimulation().currentTime()+travelTime;
		
		vehicle.setStationTransition(from, to);
		vehicle.PROCESS_STATION_TRANSITION.reset();
		vehicle.PROCESS_STATION_TRANSITION.started(getSimulation());
		vehicle.PROCESS_STATION_TRANSITION.scheduledEnd(arrivalTime);
		
		message = message.createCopy();
		message.setCode(Messages.VEHICLE_AT_STATION);
		message.setStation(to);
		hold(travelTime, message);
	}
	
	@HandleMessage(code=Messages.VEHICLE_AT_STATION)
	public void onVehicleAtStation(Message message) {
		Vehicle vehicle = message.getVehicle();
		vehicle.setCurrentStation(message.getStation());
		vehicle.PROCESS_STATION_TRANSITION.ended(getSimulation());
		
		assistantFinished(message);
	}
}
