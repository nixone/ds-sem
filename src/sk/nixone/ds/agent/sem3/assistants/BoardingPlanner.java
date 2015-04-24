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
import sk.nixone.ds.agent.sem3.model.Vehicle;

public class BoardingPlanner extends ContinualAssistant<SimulationRun, BoardingAgent>{

	public BoardingPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.BOARDING_PLANNER, mySim, myAgent);
	}

	@HandleMessage(code=Messages.start)
	public void onVehicleArrival(Message message) {
		recheck(message);
		
		/*message.setCode(Messages.WAITING_FINISHED);
		notice(message);*/
	}
	
	private void recheck(Message message) {
		Person person = message.getStation().peekFirstPerson();
		Vehicle vehicle = message.getVehicle();
		double acceptableWaitingStartTime = getSimulation().currentTime() - vehicle.getType().getNeededWaitingTime();
		
		// if person can board, board him
		if(!vehicle.isFull() && person != null && person.WAITING_FOR_BUS.getStartTime() <= acceptableWaitingStartTime) {
			Vehicle.Door door = vehicle.getAvailableDoor();
			door.occupy(person);
			
			message = message.createCopy();
			message.setPerson(person);
			message.setDoor(door);
			message.setCode(Messages.ENTERING_FINISHED);
			hold(vehicle.getType().getEntranceGenerator().next(), message);
		}
		// or if it's just full, leave and quit
		else if(vehicle.isFull()) {
			assistantFinished(message);
		}
		// or there is space but none are waiting
		else {
			message = message.createCopy();
			
		}
	}
	
	@HandleMessage(code=Messages.ENTERING_FINISHED)
	public void onEnteringFinished(Message message) {
		
	}
	
	@HandleMessage(code=Messages.WAITING_FINISHED)
	public void onWaitingFinished(Message message) {
		assistantFinished(message);
	}
}
