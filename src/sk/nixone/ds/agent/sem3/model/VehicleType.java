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
	
	public VehicleType(int capacity, int doors, int price, int gainPerPerson, double neededWaitingTime, Generator<Double> entranceGenerator, Generator<Double> exitGenerator) {
		this.capacity = capacity;
		this.doors = doors;
		this.price = price;
		this.gainPerPerson = gainPerPerson;
		this.neededWaitingTime = neededWaitingTime;
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
}
