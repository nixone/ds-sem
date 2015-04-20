package sk.nixone.ds.agent.sem3;

import OSPABA.MessageForm;
import sk.nixone.ds.agent.SimulationRun;

public class Message extends MessageForm {
	
	private Bus bus;
	
	private Person person;
	
	private Station station;
	
	public Message(SimulationRun sim) {
		super(sim);
	}

	protected Message(Message original) {
		super(original);
		station = original.station;
		bus = original.bus;
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
	
	public Bus getBus() {
		return bus;
	}
	
	public void setBus(Bus bus) {
		this.bus = bus;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
}
