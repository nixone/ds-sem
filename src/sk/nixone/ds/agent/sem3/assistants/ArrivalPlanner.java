package sk.nixone.ds.agent.sem3.assistants;

import java.util.HashMap;

import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import sk.nixone.ds.agent.ContinualAssistant;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.Station;
import sk.nixone.ds.agent.sem3.agents.SurroundingAgent;
import sk.nixone.ds.core.generators.ExponentialDelayGenerator;

public class ArrivalPlanner extends ContinualAssistant<SimulationRun, SurroundingAgent> {

	/*HashMap<String, ExponentialDelayGenerator> generators = new HashMap<String, ExponentialDelayGenerator>();
	HashMap<String, Double> latestArrivals = new HashMap<String, Double>();
	HashMap<String, Integer> capacities = new HashMap<String, Integer>();*/
	
	public ArrivalPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.ARRIVAL_PLANNER, mySim, myAgent);
		/*HashMap<String, ModelInput.Station> stationDefinitions = getSimulation().getInput().getStations();
		for(String stationName : stationDefinitions.keySet()) {
			ModelInput.Station station = stationDefinitions.get(stationName);
			if(station.getTravelers() > 0) {
				// TODO Oneskorit o T
				latestArrivals.put(stationName, 65.*60.);
				double mean = 60*65./station.getTravelers();
				generators.put(stationName, new ExponentialDelayGenerator(getSimulation().getRandoms().getNextRandom(), 1/mean));
				capacities.put(stationName, station.getTravelers());
			}
		}*/
	}
	
	@HandleMessage(code=Messages.start)
	public void onStart(Message message) {
		/*for (String stationName : generators.keySet()) {
			double d = generators.get(stationName).next();
			System.out.println(stationName+" "+d);
			hold(d, createMessage(getSimulation().getStation(stationName)));
		}*/
		hold(1, createMessage(null));
		hold(4, createMessage(null));
		hold(3, createMessage(null));
		hold(2, createMessage(null));
	}
	
	@HandleMessage(code=Messages.NEW_TRAVELER)
	public void onNewArrival(Message message) {
		System.out.println(getSimulation().currentTime());
		//Station station = message.getStation();
		//System.out.println("Received "+station.getName());
		/*double latestArrival = latestArrivals.get(station.getName());
		ExponentialDelayGenerator generator = generators.get(station.getName());
		int capacity = capacities.get(station.getName());
		
		station.addPerson(new Person(station));*/
		
		/*assistantFinished(message.createCopy());
		hold(generator.next(), createMessage(station));*/
	}
	
	private Message createMessage(Station station) {
		Message message = new Message(getSimulation());
		message.setCode(Messages.NEW_TRAVELER);
		//message.setStation(station);
		return message;
	}
}
