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
		int people = message.getVehicle().getPeopleCount();
		getSimulation().increaseServedPeople(message.getVehicle().getLine(), people);
		getSimulation().increaseServedPeople(people);
		recheck(message);
	}
	
	private void recheck(Message message) {
		Vehicle vehicle = message.getVehicle();
		
		// if person can exit, exit him
		if(vehicle.hasPeopleInside() && vehicle.hasAvailableDoors()) {
			Vehicle.Door door = vehicle.getAvailableDoor();
			Person person = vehicle.getFirstPerson();
			door.occupy(person);
			double usageTime = vehicle.getType().getExitGenerator().next();
			door.USAGE.started(getSimulation(), usageTime);
			
			Message msg = message.createCopy();
			msg.setPerson(person);
			msg.setDoor(door);
			msg.setCode(Messages.EXITING_FINISHED);
			hold(usageTime, msg);
			
			recheck(message);
		}
		// or if we are done, done...
		else if(vehicle.isEmpty() && !vehicle.hasOccupiedDoor()) {
			assistantFinished(message);
		}
	}
	
	@HandleMessage(code=Messages.EXITING_FINISHED)
	public void onEnteringFinished(Message message) {
		message.getDoor().leaveBus();
		message.getDoor().USAGE.ended(getSimulation());
		message.getDoor().USAGE.reset();
		recheck(message);
	}
}
