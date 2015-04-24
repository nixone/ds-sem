package sk.nixone.ds.agent.sem3.model;

import sk.nixone.ds.agent.ProcessMarker;

public class Person {

	public final ProcessMarker WAITING_FOR_BUS = new ProcessMarker();
	
	private Station station;
	
	private Vehicle vehicle;
	
	public Person(Station station) {
		this.station = station;
	}
	
	public Station getStation() {
		return station;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		this.station = null;
	}
	
	public void setOut() {
		setVehicle(null);
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
}
