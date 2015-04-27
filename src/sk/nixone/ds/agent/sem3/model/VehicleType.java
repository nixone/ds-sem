package sk.nixone.ds.agent.sem3.model;

import java.awt.Color;

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
	private Color color;
	
	public VehicleType(Color color, String name, int capacity, int doors, int price, int gainPerPerson, double neededWaitingTime, double waitingTimeForArrivals, Generator<Double> entranceGenerator, Generator<Double> exitGenerator) {
		this.name = name;
		this.color = color;
		this.capacity = capacity;
		this.doors = doors;
		this.price = price;
		this.gainPerPerson = gainPerPerson;
		this.neededWaitingTime = neededWaitingTime;
		this.waitingTimeForArrivals = waitingTimeForArrivals;
		this.entranceGenerator = entranceGenerator;
		this.exitGenerator = exitGenerator;
	}
	
	public Color getColor() {
		return color;
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
