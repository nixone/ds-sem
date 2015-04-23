package sk.nixone.ds.agent.sem3.model;

import java.util.HashMap;

public class Line {
	private String name;
	
	private HashMap<Station, Station> nextStations = new HashMap<Station, Station>();
	private HashMap<Station, Station> previousStations = new HashMap<Station, Station>();
	private HashMap<Station, Double> timeToNextStation = new HashMap<Station, Double>();
	private HashMap<Station, Double> timeToPreviousStation = new HashMap<Station, Double>();
	
	private Station firstStation = null;
	private Station lastStation = null;
	
	public Line(String name) {
		this.name = name;
	}
	
	public void addLeg(Station station, double time) {
		if(lastStation == null) {
			firstStation = lastStation = station;
			timeToNextStation.put(station, time);
		} else {
			timeToPreviousStation.put(station, timeToNextStation.get(lastStation));
			timeToNextStation.put(station, time);
			nextStations.put(lastStation, station);
			previousStations.put(station, lastStation);
			
			lastStation = station;
		}
	}
	
	public Station getFirstStation() {
		return firstStation;
	}
	
	public Station getLastStation() {
		return lastStation;
	}
	
	protected void updateTimesToLastStation() {
		Station current = lastStation;
		double time = 0;
		while(current != null) {
			current.setTimeToLastStation(time);
			if(previousStations.containsKey(current)) {
				time += timeToPreviousStation.get(current);
				current = previousStations.get(current);
			}
		}
	}
	
	public String getName() {
		return name;
	}
}
