package sk.nixone.ds.agent.sem3.managers;

import sk.nixone.ds.agent.Manager;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.agents.ModelAgent;

public class ModelManager extends Manager<SimulationRun, ModelAgent> {

	public ModelManager(SimulationRun simulation, ModelAgent agent) {
		super(Components.M_MODEL, simulation, agent);
	}
	
	@HandleMessage(code=Messages.INIT)
	public void onStart(Message message) {
		// notify bus movement
		message.setAddressee(getSimulation().findAgent(Components.A_VEHICLE_MOVEMENT));
		notice(message);
		
		// notify surrounding
		message = message.createCopy();
		message.setAddressee(getSimulation().findAgent(Components.A_SURROUNDING));
		notice(message);
	}
	
	@HandleMessage(code=Messages.VEHICLE_AT_STATION)
	public void onVehicleAtStation(Message message) {		
		message = message.createCopy();
		
		if(message.getStation().isBoardingStation()) {
			message.setAddressee(getSimulation().findAgent(Components.A_BOARDING));
		} else if(message.getStation().isExitingStation()) {
			message.setAddressee(getSimulation().findAgent(Components.A_EXITING));
		}
		
		notice(message);
	}
	
	@HandleMessage(code=Messages.VEHICLE_FROM_STATION)
	public void onVehicleFromStation(Message message) {
		message = message.createCopy();
		message.setAddressee(getSimulation().findAgent(Components.A_VEHICLE_MOVEMENT));
		notice(message);
	}
	
	@HandleMessage(code=Messages.NEW_TRAVELER)
	public void onNewTraveler(Message message) {
		message = message.createCopy();
		message.setAddressee(getSimulation().findAgent(Components.A_BOARDING));
		notice(message);
	}
}
