package sk.nixone.ds.agent.sem3;

import OSPABA.MessageForm;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Person;
import sk.nixone.ds.agent.sem3.model.Station;
import sk.nixone.ds.agent.sem3.model.Vehicle;
import sk.nixone.ds.agent.sem3.model.Vehicle.Door;

public class Message extends MessageForm {
	
	private Vehicle vehicle;
	
	private Person person;
	
	private Station station;
	
	private Door door;
	
	private Line line;
	
	public Message(SimulationRun sim) {
		super(sim);
	}

	protected Message(Message original) {
		super(original);
		station = original.station;
		vehicle = original.vehicle;
		person = original.person;
		door = original.door;
		line = original.line;
	}
	
	@Override
	public Message createCopy() {
		return new Message(this);
	}
	
	public Line getLine() {
		return line;
	}
	
	public void setLine(Line line) {
		this.line = line;
	}
	
	public Station getStation() {
		return station;
	}
	
	public void setDoor(Door door) {
		this.door = door;
	}
	
	public Door getDoor() {
		return door;
	}
	
	public void setStation(Station station) {
		this.station = station;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public boolean hasLine() {
		return line != null;
	}
	
	public boolean hasPerson() {
		return person != null;
	}
	
	public boolean hasVehicle() {
		return vehicle != null;
	}
	
	public boolean hasStation() {
		return station != null;
	}
	
	public boolean hasDoor() {
		return door != null;
	}
}
