package sk.nixone.ds.agent.sem3.assistants;

import OSPABA.CommonAgent;
import OSPABA.Simulation;
import sk.nixone.ds.agent.ContinualAssistant;
import sk.nixone.ds.agent.annotation.HandleMessage;
import sk.nixone.ds.agent.sem3.Components;
import sk.nixone.ds.agent.sem3.Message;
import sk.nixone.ds.agent.sem3.Messages;
import sk.nixone.ds.agent.sem3.SimulationRun;
import sk.nixone.ds.agent.sem3.agents.MovementAgent;
import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Schedule;
import sk.nixone.ds.agent.sem3.model.Vehicle;
import sk.nixone.ds.agent.sem3.model.VehicleType;

public class VehicleInitPlanner extends ContinualAssistant<SimulationRun, MovementAgent> {
	
	public VehicleInitPlanner(Simulation mySim, CommonAgent myAgent) {
		super(Components.VEHICLE_INIT_PLANNER, mySim, myAgent);
	}
	
	@HandleMessage(code=Messages.start)
	public void onStart(Message message) {
		Line line = message.getLine();
		for(VehicleType vehicleType : getAgent().getModel().getVehicleTypes()) {
			Schedule schedule = getAgent().getModel().findSchedule(line, vehicleType);
			for(double time : schedule) {
				Vehicle vehicle = new Vehicle(vehicleType);
				vehicle.setLine(line);
				getAgent().getModel().getVehicles().add(vehicle);
				
				message = message.createCopy();
				message.setVehicle(vehicle);
				message.setStation(line.getFirstStation());
				message.setCode(Messages.VEHICLE_INIT_FINISHED);
				hold(time, message);
			}
		}
	}
	
	@HandleMessage(code=Messages.VEHICLE_INIT_FINISHED)
	public void onEnd(Message message) {
		assistantFinished(message);
	}
}
