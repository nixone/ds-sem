package sk.nixone.ds.agent.sem3.model;

public class Person {

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
