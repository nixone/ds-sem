package sk.nixone.ds.agent.sem3.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import sk.nixone.ds.core.generators.Generator;

public class Station {
	private String name;
	private double timeToLastStation;
	private boolean exitingStation = false;
	
	private LinkedList<Person> people = new LinkedList<Person>();
	private int peopleArrivedCount = 0;
	
	private int peopleArrivalCount;
	
	private Generator<Double> arrivalDistanceGenerator = null;
	
	private double earliestArrival;
	
	private double latestArrival;
	
	private HashSet<Vehicle> vehicles = new HashSet<Vehicle>();
	
	public Station(String name, int capacity) {
		this.name = name;
		peopleArrivalCount = capacity;
	}
	
	public HashSet<Vehicle> getVehicles() {
		return vehicles;
	}
	
	public boolean isBoardingStation() {
		return !exitingStation;
	}
	
	public boolean isExitingStation() {
		return exitingStation;
	}
	
	public void setPeopleArrivalCount(int count) {
		peopleArrivalCount = count;
	}
	
	public int getPeopleCapacity() {
		return peopleArrivalCount;
	}
	
	public Person peekFirstPerson() {
		return people.peekFirst();
	}
	
	public Person getFirstPerson() {
		return people.pollFirst();
	}
	
	public String getName() {
		return name;
	}
	
	public void setTimeToLastStation(double time) {
		timeToLastStation = time;
	}
	
	public double getTimeToLastStation() {
		return timeToLastStation;
	}
	
	public void setArrivalInterval(double earliest, double latest) {
		earliestArrival = earliest;
		latestArrival = latest;
	}
	
	public int getCurrentPeopleCount() {
		return people.size();
	}
	
	public ArrayList<Person> getCurrentPeople() {
		return new ArrayList<Person>(people);
	}
	
	public void addPerson(Person person) {
		peopleArrivedCount++;
		people.addLast(person);
	}
	
	public void setArrivalDistanceGenerator(Generator<Double> generator) {
		arrivalDistanceGenerator = generator;
	}
	
	public double nextArrival() {
		if(arrivalDistanceGenerator == null) {
			throw new NullPointerException("Generator is not set for this station yet.");
		}
		return arrivalDistanceGenerator.next();
	}
	
	public boolean canPeopleArrive() {
		return peopleArrivedCount < peopleArrivalCount;
	}
	
	public double getEarliestArrival() {
		return earliestArrival;
	}
	
	public double getLatestArrival() {
		return latestArrival;
	}
	
	public void reset() {
		people.clear();
		peopleArrivedCount = 0;
	}
	
	public void setExitingStation() {
		exitingStation = true;
	}
}
