package sk.nixone.ds.agent.sem3.assistants;

import OSPABA.CommonAgent;
import OSPABA.Simulation;
import sk.nixone.ds.agent.ContinualAssistant;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.agents.ExitingAgent;
import sk.nixone.ds.agent.sem3.model.Person;
import sk.nixone.ds.agent.sem3.model.Vehicle;

public class ExitingPlanner extends ContinualAssistant<SimulationRun, ExitingAgent> {

	public ExitingPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.EXITING_PLANNER, mySim, myAgent);
	}

	@HandleMessage(code=Messages.start)
	public void onVehicleArrival(Message message) {
		recheck(message);
	}
	
	private void recheck(Message message) {
		Vehicle vehicle = message.getVehicle();
		
		// if person can exit, exit him
		if(!vehicle.isEmpty() && vehicle.hasAvailableDoors()) {
			Vehicle.Door door = vehicle.getAvailableDoor();
			Person person = vehicle.getFirstPerson();
			door.occupy(person);
			
			message = message.createCopy();
			message.setPerson(person);
			message.setDoor(door);
			message.setCode(Messages.EXITING_FINISHED);
			hold(vehicle.getType().getExitGenerator().next(), message);
		}
		// or if we are done, done...
		else if(vehicle.isEmpty() && !vehicle.hasOccupiedDoor()) {
			assistantFinished(message);
		}
	}
	
	@HandleMessage(code=Messages.EXITING_FINISHED)
	public void onEnteringFinished(Message message) {
		message.getDoor().leaveBus();
		recheck(message);
	}
}
