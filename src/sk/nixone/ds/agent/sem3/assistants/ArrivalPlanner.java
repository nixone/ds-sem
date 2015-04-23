package sk.nixone.ds.agent.sem3.assistants;

import OSPABA.CommonAgent;
import OSPABA.Simulation;
import sk.nixone.ds.agent.ContinualAssistant;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.agents.SurroundingAgent;
import sk.nixone.ds.agent.sem3.model.Person;
import sk.nixone.ds.agent.sem3.model.Station;

public class ArrivalPlanner extends ContinualAssistant<SimulationRun, SurroundingAgent> {
	
	public ArrivalPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.ARRIVAL_PLANNER, mySim, myAgent);
	}
	
	@HandleMessage(code=Messages.start)
	public void onStart(Message message) {
		for (Station station : getAgent().getStations()) {
			hold(station.nextArrival(), createMessage(station));
		}
	}
	
	@HandleMessage(code=Messages.NEW_TRAVELER)
	public void onNewArrival(Message message) {
		Station station = message.getStation();
		double time = getSimulation().currentTime();
		
		if(time <= station.getLatestArrival()) {
			if(time >= station.getEarliestArrival() && station.canPeopleArrive()) {
				Person person = new Person(station);
				station.addPerson(person);
			}
			
			Message msg = createMessage(station);
			hold(station.nextArrival(), msg);
			assistantFinished(msg.createCopy());
		}
	}
	
	private Message createMessage(Station station) {
		Message message = new Message(getSimulation());
		message.setCode(Messages.NEW_TRAVELER);
		message.setStation(station);
		return message;
	}
}
