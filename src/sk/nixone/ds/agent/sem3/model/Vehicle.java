package sk.nixone.ds.agent.sem3.model;

import java.util.LinkedList;

import sk.nixone.ds.agent.ProcessMarker;

public class Vehicle {
	
	public class Door {
		
		private Person occupiedBy = null;
		
		public void occupy(Person person) {
			occupiedBy = person;
			freeDoorCount--;
		}
		
		public void getIn() {
			people.addLast(occupiedBy);
			occupiedBy = null;
			freeDoorCount++;
		}
		
		public void leaveBus() {
			occupiedBy = null;
			freeDoorCount++;
		}
	}
	
	public final ProcessMarker STATION_TRANSITION = new ProcessMarker();
	
	public final ProcessMarker WAITING_FOR_ARRIVALS = new ProcessMarker();
	
	private VehicleType type;
	
	private Line line;
	
	private Station currentStation;
	
	private Station goingFrom;
	
	private Station goingTo;
	
	private Door[] doors;
	
	private LinkedList<Person> people = new LinkedList<Person>();
	
	private int freeDoorCount;
	
	private int totalDoorCount;
	
	public Vehicle(VehicleType type) {
		this.type = type;
		freeDoorCount = totalDoorCount = type.getDoorCount();
		doors = new Door[totalDoorCount];
		for(int i=0; i<type.getDoorCount(); i++) {
			doors[i] = new Door();
		}
		people = new LinkedList<Person>();
	}
	
	public int getPeopleCount() {
		return people.size();
	}
	
	public boolean hasAvailableDoors() {
		return freeDoorCount > 0;
	}
	
	public boolean hasOccupiedDoor() {
		return freeDoorCount != totalDoorCount;
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
		if(currentStation != null) {
			currentStation.getVehicles().remove(this);
			currentStation = null;
		}
	}
	
	public void setCurrentStation(Station station) {
		currentStation = station;
		goingFrom = goingTo = null;
		currentStation.getVehicles().add(this);
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
	
	public Person getFirstPerson() {
		return people.removeFirst();
	}
	
	public boolean isFull() {
		return people.size() >= getType().getCapacity();
	}
	
	public boolean isEmpty() {
		return people.size() == 0;
	}
	
	public double getFullness() {
		if(isFull()) {
			return 1;
		}
		if(isEmpty()) {
			return 0;
		}
		return (double)people.size() / getType().getCapacity();
	}
}
