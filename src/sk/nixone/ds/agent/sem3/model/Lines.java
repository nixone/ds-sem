package sk.nixone.ds.agent.sem3.model;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class Lines extends LinkedHashMap<String, Line> implements Iterable<Line> {

	public void add(Line line) {
		put(line.getName(), line);
	}
	
	public Line find(String name) {
		return get(name);
	}

	@Override
	public Iterator<Line> iterator() {
		return values().iterator();
	}
	
	public void updateTimesToLastStation() {
		for(Line line : this) {
			line.updateTimesToLastStation();
		}
	}
	
	public void setStationLineOwnership() {
		for(Line line : this) {
			for(Station station : line) {
				station.getLines().add(line);
			}
		}
	}
	
	public void reset() {
		// nothing to do
	}
}
