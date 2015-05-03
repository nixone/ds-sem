package sk.nixone.ds.agent.sem3.model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.generators.DummyDoubleGenerator;
import sk.nixone.ds.core.generators.ExponentialDelayGenerator;
import sk.nixone.ds.core.generators.Generator;
import sk.nixone.ds.core.generators.TriangleGenerator;
import sk.nixone.ds.core.generators.UniformGenerator;
import sk.nixone.util.HashablePair;

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
	private HashMap<HashablePair<Line, VehicleType>, Schedule> schedules = new HashMap<HashablePair<Line, VehicleType>, Schedule>();
	private HashMap<HashablePair<Line, VehicleType>, Schedule> schedulesForWaiting = new HashMap<HashablePair<Line, VehicleType>, Schedule>();
	private boolean waitingStrategy = false;
	
	public Model(File linesFile, Randoms randoms) throws IOException {
		Generator<Double> busGenerator = new TriangleGenerator(randoms.getNextRandom(), 0.6, 1.2, 3.2);
		Generator<Double> microBusEntranceGenerator = new UniformGenerator(randoms.getNextRandom(), 6, 10);
		Generator<Double> microBusExitGenerator = new DummyDoubleGenerator(4);
		
		vehicleTypes = new VehicleTypes(
			new VehicleType(Color.GREEN, "Bus 1", 186, 4, 17780000, 0, 0, 0, busGenerator, busGenerator),
			new VehicleType(Color.BLUE, "Bus 2", 107, 3, 6450000, 0, 0, 0, busGenerator, busGenerator),
			new VehicleType(Color.RED, "Microbus", 8, 1, 0, 30, 360, 0, microBusEntranceGenerator, microBusExitGenerator)
		);

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(linesFile)))) {
			String ln = null;
			while((ln = reader.readLine()) != null) {
				String lineName = ln;
				Line line = new Line(lineName, vehicleTypes);
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
		lines.setStationLineOwnership();
		
		Station furthestStation = stations.getStationWithLongestTravel();
		matchStartTime = furthestStation.getTimeToLastStation()+ARRIVAL_TIME_OFFSET_START;
		
		for(Station station : stations) {
			double start = matchStartTime-ARRIVAL_TIME_OFFSET_START-station.getTimeToLastStation();
			double stop = matchStartTime-ARRIVAL_TIME_OFFSET_STOP-station.getTimeToLastStation();
			double duration = stop-start;
			
			station.setArrivalInterval(start, stop);
			station.setArrivalDistanceGenerator(new ExponentialDelayGenerator(randoms.getNextRandom(), station.getPeopleCapacity() / duration));
		}
		
		for(Line line : lines) {
			line.getLastStation().setExitingStation();
		}
		
		for(Line line : lines) {
			for(VehicleType vehicleType : vehicleTypes) {
				schedules.put(new HashablePair<Line, VehicleType>(line, vehicleType), new Schedule(this, line, vehicleType));
				schedulesForWaiting.put(new HashablePair<Line, VehicleType>(line, vehicleType), new Schedule(this, line, vehicleType));
			}
		}
		setWaitingStrategy(false);
	}
	
	public void setWaitingStrategy(boolean waitingStrategy) {
		this.waitingStrategy = waitingStrategy;
		double t = waitingStrategy ? 90 : 0;
		vehicleTypes.BUS_1.setWaitingTimeForArrivals(t);
		vehicleTypes.BUS_2.setWaitingTimeForArrivals(t);
		if(waitingStrategy) {
			for(Schedule schedule : schedulesForWaiting.values()) {
				schedule.fromExpression(schedule.getExpression());
			}
		} else {
			for(Schedule schedule : schedules.values()) {
				schedule.fromExpression(schedule.getExpression());
			}
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
	
	public Schedule findSchedule(Line line, VehicleType vehicleType) {
		HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(line, vehicleType);
		return waitingStrategy ? schedulesForWaiting.get(p) : schedules.get(p);
	}
	
	public void reset() {
		lines.reset();
		stations.reset();
		vehicles.reset();
	}
	
	public double getTotalDepartingTime(Line line) {
		return matchStartTime - line.getTimeToLastStation();
	}
	
	public void loadSchedules(File file) throws IOException {
		try(Scanner scanner = new Scanner(file)) {
			while(scanner.hasNextLine()) {
				boolean waiting = Boolean.parseBoolean(scanner.nextLine());
				String lineName = scanner.nextLine();
				String vehicleTypeName = scanner.nextLine();
				
				HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(lines.find(lineName), vehicleTypes.find(vehicleTypeName));
				Schedule schedule = waiting ? schedulesForWaiting.get(p) : schedules.get(p);
				schedule.clear();
				
				schedule.fromExpression(scanner.nextLine());
			}
		}
	}
	
	public void saveSchedules(File file) throws IOException {
		PrintStream out = new PrintStream(file);
		
		for(Line line : lines) {
			for(VehicleType type : vehicleTypes) {
				out.println(false);
				out.println(line.getName());
				out.println(type.getName());
				
				HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(line, type);
				Schedule schedule = schedules.get(p);
				out.println(schedule.getExpression());
				
				out.println(true);
				out.println(line.getName());
				out.println(type.getName());
				
				p = new HashablePair<Line, VehicleType>(line, type);
				schedule = schedulesForWaiting.get(p);
				out.println(schedule.getExpression());
			}
		}
		
		out.close();
	}
	
	public int getPrice() {
		int price = 0;
		for(Line line : lines) {
			for(VehicleType type : vehicleTypes) {
				Schedule schedule = findSchedule(line, type);
				price += schedule.size() * type.getPrice();
			}
		}
		return price;
	}
}
