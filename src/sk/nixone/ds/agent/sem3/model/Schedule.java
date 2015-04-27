package sk.nixone.ds.agent.sem3.model;

import java.util.LinkedList;

public class Schedule extends LinkedList<Double> {
	
	private Line line;
	
	private VehicleType vehicleType;
	
	public Schedule(Line line, VehicleType vehicleType) {
		super();
		this.line = line;
		this.vehicleType = vehicleType;
	}
	
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	
	public Line getLine() {
		return line;
	}
}
