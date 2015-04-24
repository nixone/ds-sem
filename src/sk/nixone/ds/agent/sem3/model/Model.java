package sk.nixone.ds.agent.sem3.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.generators.DummyDoubleGenerator;
import sk.nixone.ds.core.generators.ExponentialDelayGenerator;
import sk.nixone.ds.core.generators.Generator;
import sk.nixone.ds.core.generators.TriangleGenerator;
import sk.nixone.ds.core.generators.UniformGenerator;

public class Model {

	public static final double ARRIVAL_TIME_OFFSET_START = 75*60.;
	public static final double ARRIVAL_TIME_OFFSET_STOP = 10*60.;
	
	private static double parse(String str) {
		try {
			return Double.parseDouble(str);
		} catch(NumberFormatException e) {
			return Double.parseDouble(str.replace(",", "."));
		}
	}
	
	private Lines lines = new Lines();
	private Stations stations = new Stations();
	private Vehicles vehicles = new Vehicles();
	private VehicleTypes vehicleTypes;
	private double matchStartTime;
	
	public Model(File path, Randoms randoms) throws IOException {
		Generator<Double> busGenerator = new TriangleGenerator(randoms.getNextRandom(), 0.6, 1.2, 3.2);
		Generator<Double> microBusEntranceGenerator = new UniformGenerator(randoms.getNextRandom(), 6, 10);
		Generator<Double> microBusExitGenerator = new DummyDoubleGenerator(4);
		
		vehicleTypes = new VehicleTypes(
			new VehicleType(186, 4, 17780000, 0, 0, busGenerator, busGenerator),
			new VehicleType(107, 3, 6450000, 0, 0, busGenerator, busGenerator),
			new VehicleType(8, 1, 0, 30, 360, microBusEntranceGenerator, microBusExitGenerator)
		);

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
			String ln = null;
			while((ln = reader.readLine()) != null) {
				String lineName = ln;
				Line line = new Line(lineName);
				while(((ln = reader.readLine()) != null) && !ln.contentEquals("")) {
					String stationName = ln;
					double duration = parse(reader.readLine());
					int travelers = Integer.parseInt(reader.readLine());
					
					Station station = stations.find(stationName);
					if(station == null) {
						stations.add(station = new Station(stationName, travelers));
					}
					line.addLeg(station, duration*60);
				}
				lines.put(lineName, line);
			}
		}
		
		lines.updateTimesToLastStation();		
		
		Station furthestStation = stations.getStationWithLongestTravel();
		matchStartTime = furthestStation.getTimeToLastStation()+ARRIVAL_TIME_OFFSET_START;
		
		for(Station station : stations) {
			double start = matchStartTime-ARRIVAL_TIME_OFFSET_START-station.getTimeToLastStation();
			double stop = matchStartTime-ARRIVAL_TIME_OFFSET_STOP-station.getTimeToLastStation();
			double duration = stop-start;
			
			station.setArrivalInterval(start, stop);
			station.setArrivalDistanceGenerator(new ExponentialDelayGenerator(randoms.getNextRandom(), station.getPeopleCapacity() / duration));
		}
	}
	
	public Lines getLines() {
		return lines;
	}
	
	public Stations getStations() {
		return stations;
	}
	
	public double getMatchStartTime() {
		return matchStartTime;
	}
	
	public Vehicles getVehicles() {
		return vehicles;
	}
	
	public VehicleTypes getVehicleTypes() {
		return vehicleTypes;
	}
	
	public void reset() {
		lines.reset();
		stations.reset();
		vehicles.reset();
		
		for(Line line : lines) {
			Vehicle vehicle = new Vehicle(vehicleTypes.MICROBUS);
			vehicle.setLine(line);
			vehicles.add(vehicle);
		}
	}
}