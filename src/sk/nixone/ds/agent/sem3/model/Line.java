package sk.nixone.ds.agent.sem3.model;

import java.util.HashMap;
import java.util.Iterator;

import sk.nixone.ds.core.ui.property.DoubleProperty;
import sk.nixone.ds.core.ui.property.IntegerProperty;

public class Line implements Iterable<Station> {	
	public class LineIterator implements Iterator<Station> {

		private boolean first = true;
		private Station current = null;
		
		@Override
		public boolean hasNext() {
			return (first && firstStation != null) || nextStations.containsKey(current);
		}

		@Override
		public Station next() {
			if(first) {
				first = false;
				return current = firstStation;
			}
			return current = nextStations.get(current);
		}
	}
	
	private String name;
	
	private HashMap<Station, Station> nextStations = new HashMap<Station, Station>();
	private HashMap<Station, Station> previousStations = new HashMap<Station, Station>();
	private HashMap<Station, Double> timeToNextStation = new HashMap<Station, Double>();
	private HashMap<Station, Double> timeToPreviousStation = new HashMap<Station, Double>();
	
	private Station firstStation = null;
	private Station lastStation = null;
	
	public Line(String name, VehicleTypes vehicleTypes) {
		this.name = name;
	}
	
	public Station getNextStation(Station current, boolean circle) {
		Station realNext = nextStations.get(current);
		if(realNext == null && circle) {
			return firstStation;
		}
		return realNext;
	}
	
	public double getNextStationTime(Station current) {
		return timeToNextStation.get(current);
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
			} else {
				break;
			}
		}
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Iterator<Station> iterator() {
		return new LineIterator();
	}
	
	public double getTimeToLastStation() {
		return getFirstStation().getTimeToLastStation();
	}
}
