package sk.nixone.ds.agent.sem3.model;

import java.util.HashMap;
import java.util.Iterator;

public class Lines extends HashMap<String, Line> implements Iterable<Line> {

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
}
