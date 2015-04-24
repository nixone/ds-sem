package sk.nixone.ds.agent.sem3.model;

import java.util.ArrayList;

import sk.nixone.ds.agent.ProcessMarker;

public class Vehicle {
	
	public class Door {
		public final ProcessMarker PROCESS_GETTING_IN = new ProcessMarker();
		
		private Person occupiedBy = null;
		
		public void occupy(Person person) {
			occupiedBy = person;
		}
		
		public void getIn() {
			people.add(occupiedBy);
			occupiedBy = null;
		}
	}
	
	public final ProcessMarker PROCESS_STATION_TRANSITION = new ProcessMarker();
	
	private VehicleType type;
	
	private Line line;
	
	private Station currentStation;
	
	private Station goingFrom;
	
	private Station goingTo;
	
	private Door[] doors;
	
	private ArrayList<Person> people = null;
	
	public Vehicle(VehicleType type) {
		this.type = type;
		doors = new Door[type.getDoorCount()];
		for(int i=0; i<type.getDoorCount(); i++) {
			doors[i] = new Door();
		}
		people = new ArrayList<Person>();
	}
	
	public boolean hasAvailableDoors() {
		return getAvailableDoor() == null;
	}
	
	public Door getAvailableDoor() {
		for(int i=0; i<doors.length; i++) {
			if(doors[i].occupiedBy == null) {
				return doors[i];
			}
		}
		return null;
	}
	
	public Door[] getDoors() {
		return doors;
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
