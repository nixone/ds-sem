package sk.nixone.ds.agent.sem3.model;

import sk.nixone.ds.core.generators.Generator;

public class VehicleType {
	
	private int capacity;
	private int doors;
	private int price;
	private int gainPerPerson;
	private Generator<Double> entranceGenerator;
	private Generator<Double> exitGenerator;
	private double neededWaitingTime;
	private double waitingTimeForArrivals;
	private String name;
	
	public VehicleType(String name, int capacity, int doors, int price, int gainPerPerson, double neededWaitingTime, double waitingTimeForArrivals, Generator<Double> entranceGenerator, Generator<Double> exitGenerator) {
		this.name = name;
		this.capacity = capacity;
		this.doors = doors;
		this.price = price;
		this.gainPerPerson = gainPerPerson;
		this.neededWaitingTime = neededWaitingTime;
		this.waitingTimeForArrivals = waitingTimeForArrivals;
		this.entranceGenerator = entranceGenerator;
		this.exitGenerator = exitGenerator;
	}
	
	public String getName() {
		return name;
	}
	
	public Generator<Double> getEntranceGenerator() {
		return entranceGenerator;
	}
	
	public Generator<Double> getExitGenerator() {
		return exitGenerator;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getDoorCount() {
		return doors;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getGainPerPerson() {
		return gainPerPerson;
	}
	
	public double getNeededWaitingTime() {
		return neededWaitingTime;
	}
	
	public double getWaitingTimeForArrivals() {
		return waitingTimeForArrivals;
	}
}
