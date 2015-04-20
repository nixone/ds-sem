package sk.nixone.ds.agent.sem3;

import java.util.HashSet;

public class Station {

	private String name;
	private HashSet<Person> people = new HashSet<Person>();
	private int generatedPeople = 0;
	
	public Station(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addPerson(Person person) {
		generatedPeople++;
		people.add(person);
	}
	
	public void removePerson(Person person) {
		people.remove(person);
	}
	
	public int getPeopleCount() {
		return people.size();
	}
	
	public int getGeneratedPeopleCount() {
		return generatedPeople;
	}
}
