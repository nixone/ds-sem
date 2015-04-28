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
import sk.nixone.ds.agent.sem3.model.Person;
import sk.nixone.ds.agent.sem3.model.Station;
import sk.nixone.ds.agent.sem3.model.Vehicle;

public class BoardingPlanner extends ContinualAssistant<SimulationRun, BoardingAgent>{

	public BoardingPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.BOARDING_PLANNER, mySim, myAgent);
	}

	@HandleMessage(code=Messages.start)
	public void onVehicleArrival(Message message) {
		// it was started because vehicle arrived
		if(message.hasVehicle()) {
			message.getVehicle().WAITING_FOR_ARRIVALS.reset();
			recheck(message);
		}
		// it was started because traveler arrived
		else {
			Station station = message.getStation();
			for(Vehicle vehicle : station.getVehicles()) {
				Message msg = message.createCopy();
				msg.setVehicle(vehicle);
				recheck(msg);
			}
		}
	}
	
	private void recheck(Message message) {
		Station station = message.getStation();
		Person person = station.peekFirstPerson();
		Vehicle vehicle = message.getVehicle();
		double acceptableWaitingStartTime = getSimulation().currentTime() - vehicle.getType().getNeededWaitingTime();
		
		// if person can board, board him
		if(vehicle.hasAvailableDoors() && !vehicle.isFull() && person != null && person.WAITING_FOR_BUS.getStartTime() <= acceptableWaitingStartTime) {
			person.WAITING_FOR_BUS.ended(getSimulation());
			getSimulation().getPersonWaitingTime().add(person.WAITING_FOR_BUS.getDuration());
			
			Vehicle.Door door = vehicle.getAvailableDoor();
			double usageDuration = vehicle.getType().getEntranceGenerator().next();
			door.occupy(station.getFirstPerson());
			door.USAGE.started(getSimulation(), usageDuration);
			
			Message msg = message.createCopy();
			msg.setPerson(person);
			msg.setDoor(door);
			msg.setCode(Messages.ENTERING_FINISHED);
			hold(usageDuration, msg);
			
			recheck(message);
		}
		// if it should immediately leave
		else if(!vehicle.hasOccupiedDoor() && (vehicle.isFull() || vehicle.WAITING_FOR_ARRIVALS.didFinish())) {
			// if we are waiting "cancel it"
			if(vehicle.WAITING_FOR_ARRIVALS.didHappen() && !vehicle.WAITING_FOR_ARRIVALS.didFinish()) {
				vehicle.WAITING_FOR_ARRIVALS.ended(getSimulation());
			}
			assistantFinished(message);
		}
		// or there is space but none are waiting
		else if(!vehicle.hasOccupiedDoor() && !vehicle.isFull() && !vehicle.WAITING_FOR_ARRIVALS.didHappen() ) {
			double t = vehicle.getType().getWaitingTimeForArrivals();
			vehicle.WAITING_FOR_ARRIVALS.started(getSimulation(), t);
			
			message = message.createCopy();
			message.setCode(Messages.WAITING_FINISHED);
			hold(t, message);
		}
	}
	
	@HandleMessage(code=Messages.ENTERING_FINISHED)
	public void onEnteringFinished(Message message) {
		message.getDoor().getIn();
		message.getDoor().USAGE.ended(getSimulation());
		message.getDoor().USAGE.reset();
		getSimulation().increaseGain(message.getVehicle().getType().getGainPerPerson());
		recheck(message);
	}
	
	@HandleMessage(code=Messages.WAITING_FINISHED)
	public void onWaitingFinished(Message message) {
		Vehicle vehicle = message.getVehicle();
		
		// if we didn't cancel the wait so far
		if(vehicle.WAITING_FOR_ARRIVALS.didHappen() && !vehicle.WAITING_FOR_ARRIVALS.didFinish()) {
			vehicle.WAITING_FOR_ARRIVALS.ended(getSimulation());
			recheck(message);
		}
	}
}
