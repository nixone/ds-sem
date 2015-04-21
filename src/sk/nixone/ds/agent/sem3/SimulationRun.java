package sk.nixone.ds.agent.sem3;

import java.util.HashMap;
import java.util.HashSet;

import sk.nixone.ds.agent.sem3.agents.BusMovementAgent;
import sk.nixone.ds.agent.sem3.agents.ModelAgent;
import sk.nixone.ds.agent.sem3.agents.SurroundingAgent;
import sk.nixone.ds.core.Randoms;

public class SimulationRun extends sk.nixone.ds.agent.SimulationRun {
	
	private ModelAgent modelAgent;
	
	private BusMovementAgent busMovementAgent;
	
	private SurroundingAgent surroundingAgent;
	
	private ModelInput input;
	
	private HashMap<String, Station> stations = new HashMap<String, Station>();
	
	private HashSet<Bus> buses = new HashSet<Bus>();
	
	private Randoms randoms;
	
	public SimulationRun(Randoms randoms, ModelInput input) {
		super();
		/*this.input = input;
		this.randoms = randoms;*/
		modelAgent = new ModelAgent(this);
		busMovementAgent = new BusMovementAgent(this, modelAgent);
		surroundingAgent = new SurroundingAgent(this, modelAgent);
		/*
		HashMap<String, ModelInput.Station> stationDescriptions = input.getStations();
		HashMap<String, ModelInput.Line> lineDescriptions = input.getLines();
		
		for(String stationName : stationDescriptions.keySet()) {
			stations.put(stationName, new Station(stationName));
		}
		for(String lineName : lineDescriptions.keySet()) {
			ModelInput.Line lineDescription = lineDescriptions.get(lineName);
			buses.add(new Bus(lineName, getStation(lineDescription.getStationNames().get(0))));
		}*/
	}
	
	public Randoms getRandoms() {
		return randoms;
	}
	
	public Station getStation(String name) {
		return stations.get(name);
	}
	
	public ModelInput getInput() {
		return input;
	}
	
	public ModelAgent getModelAgent() {
		return modelAgent;
	}
	
	public BusMovementAgent getBusMovementAgent() {
		return busMovementAgent;
	}
	
	public SurroundingAgent getSurroundingAgent() {
		return surroundingAgent;
	}
}
