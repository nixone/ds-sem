package sk.nixone.ds.agent.sem3.model;

import java.util.HashMap;
import java.util.Iterator;

public class Stations extends HashMap<String, Station> implements Iterable<Station> {

	@Override
	public Iterator<Station> iterator() {
		return values().iterator();
	}
	
	public void add(Station station) {
		put(station.getName(), station);
	}
	
	public Station find(String name) {
		return get(name);
	}

	public Station getStationWithLongestTravel() {
		Station result = null;
		for(Station station : this) {
			if(result == null || result.getTimeToLastStation() < station.getTimeToLastStation()) {
				result = station;
			}
		}
		return result;
	}
	
	public void reset() {
		for(Station station : this) {
			station.reset();
		}
	}
}
