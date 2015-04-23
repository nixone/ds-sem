package sk.nixone.ds.agent.sem3;

import OSPABA.MessageForm;
import sk.nixone.ds.agent.SimulationRun;
import sk.nixone.ds.agent.sem3.model.Person;
import sk.nixone.ds.agent.sem3.model.Station;
import sk.nixone.ds.agent.sem3.model.Vehicle;

public class Message extends MessageForm {
	
	private Vehicle vehicle;
	
	private Person person;
	
	private Station station;
	
	public Message(SimulationRun sim) {
		super(sim);
	}

	protected Message(Message original) {
		super(original);
		station = original.station;
		vehicle = original.vehicle;
		person = original.person;
	}
	
	@Override
	public Message createCopy() {
		return new Message(this);
	}
	
	public Station getStation() {
		return station;
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
}
