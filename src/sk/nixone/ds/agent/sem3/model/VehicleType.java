package sk.nixone.ds.agent.sem3.model;

public enum VehicleType {
	
	BUS_1(186, 4, 17780000, 0),
	BUS_2(107, 3, 6450000, 0),
	MICROBUS(8, 1, 0, 30);
	
	private int capacity;
	private int doors;
	private int price;
	private int gainPerPerson;
	
	VehicleType(int capacity, int doors, int price, int gainPerPerson) {
		this.capacity = capacity;
		this.doors = doors;
		this.price = price;
		this.gainPerPerson = gainPerPerson;
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
}
