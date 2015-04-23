package sk.nixone.ds.agent.sem3.ui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import sk.nixone.ds.agent.sem3.model.Station;

public class StationsLayout {
	
	private HashMap<String, Position> positions = new HashMap<String, Position>();
	
	public StationsLayout(File file) throws IOException {
		try(Scanner scanner = new Scanner(file)) {
			while(scanner.hasNext()) {
				positions.put(scanner.next(), new Position(scanner.nextDouble(), scanner.nextDouble()));
				if(scanner.hasNext()) {
					scanner.nextLine();
				}
			}
		}
	}
	
	public Position getPosition(Station station) {
		if(!positions.containsKey(station.getName())) {
			throw new IllegalArgumentException("Can't find position for "+station.getName());
		}
		
		return positions.get(station.getName());
	}
}
