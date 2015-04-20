package sk.nixone.ds.agent.sem3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ModelInput {

	private static double parse(String str) {
		try {
			return Double.parseDouble(str);
		} catch(NumberFormatException e) {
			return Double.parseDouble(str.replace(",", "."));
		}
	}
	
	public class Line {
		private String name;
		private LinkedHashMap<String, Double> durationsToNextStation;
		
		public Line(String name) {
			this.name = name;
			this.durationsToNextStation = new LinkedHashMap<String, Double>();
		}
		
		public String getName() {
			return name;
		}
		
		public List<String> getStationNames() {
			ArrayList<String> result = new ArrayList<String>(durationsToNextStation.size());
			for(String station : durationsToNextStation.keySet()) {
				result.add(station);
			}
			return result;
		}
		
		public List<Double> getDurations() {
			ArrayList<Double> result = new ArrayList<Double>(durationsToNextStation.size());
			for(String station : durationsToNextStation.keySet()) {
				result.add(durationsToNextStation.get(station));
			}
			return result;
		}
	}
	
	public class Station {
		private String name;
		
		private int travelers;
		
		public Station(String name, int travelers) {
			this.name = name;
			this.travelers = travelers;
		}
		
		public String getName() {
			return name;
		}
		
		public int getTravelers() {
			return travelers;
		}
	}
	
	private HashMap<String, Line> lines = new HashMap<String, Line>();
	private HashMap<String, Station> stations = new HashMap<String, Station>();
	private HashMap<String, Double> stationToStadiumTimes = new HashMap<String, Double>();
	
	public ModelInput(File path) throws IOException {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
			String ln = null;
			while((ln = reader.readLine()) != null) {
				String lineName = ln;
				Line line = new Line(lineName);
				while(((ln = reader.readLine()) != null) && !ln.contentEquals("")) {
					String stationName = ln;
					double duration = parse(reader.readLine());
					int travelers = Integer.parseInt(reader.readLine());
					
					line.durationsToNextStation.put(stationName, duration);
					if (!stations.containsKey(stationName)) {
						Station station = new Station(stationName, travelers);
						stations.put(stationName, station);
					}
				}
				lines.put(lineName, line);
			}
		}
		
		// TODO stationToStadiumTimes
	}
	
	public HashMap<String, Line> getLines() {
		return lines;
	}
	
	public HashMap<String, Station> getStations() {
		return stations;
	}
	
	public HashMap<String, Double> getStationToStadiumTimes() {
		return stationToStadiumTimes;
	}
}
