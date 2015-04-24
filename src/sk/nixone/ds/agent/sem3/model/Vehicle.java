package sk.nixone.ds.agent.sem3.model;

import sk.nixone.ds.agent.ProcessMarker;

public class Vehicle {
	
	public final ProcessMarker PROCESS_STATION_TRANSITION = new ProcessMarker();
	
	private VehicleType type;
	
	private Line line;
	
	private Station currentStation;
	
	private Station goingFrom;
	
	private Station goingTo;
	
	public Vehicle(VehicleType type) {
		this.type = type;
	}
	
	public void setLine(Line line) {
		this.line = line;
	}
	
	public void setStationTransition(Station from, Station to) {
		goingFrom = from;
		goingTo = to;
		currentStation = null;
	}
	
	public void setCurrentStation(Station station) {
		currentStation = station;
		goingFrom = goingTo = null;
	}
	
	public Line getLine() {
		return line;
	}
	
	public VehicleType getType() {
		return type;
	}
	
	public Station getCurrentStation() {
		return currentStation;
	}
	
	public Station getStationGoingFrom() {
		return goingFrom;
	}
	
	public Station getStationGoingTo() {
		return goingTo;
	}
}
