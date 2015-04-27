package sk.nixone.ds.agent.sem3.ui;

import java.awt.Color;
import java.util.HashSet;

import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Station;
import sk.nixone.ds.agent.sem3.model.Vehicle;
import sk.nixone.ds.agent.sem3.model.VehicleType;
import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;

public class StationsCanvas extends HelperCanvas {

	private Emitter<Double> repainter = new DelayedEmitter<Double>(new Emitter<Double>() {

		@Override
		public void reset() {
			displaying = false;
		}

		@Override
		public void emit(Double value) {
			displaying = true;
			simulationTime = value;
			repaint();
		}
	
	}, 15);
	
	private StationsLayout layout;
	private	Model model;
	private double simulationTime;
	private StationCanvas stationCanvas;
	
	public StationsCanvas(Model model, StationsLayout layout) {
		this.model = model;
		this.layout = layout;
	}
	
	public void setStationCanvas(StationCanvas stationCanvas) {
		this.stationCanvas = stationCanvas;
	}

	@Override
	public void paintDisplay() {
		resetPosition();
		resetDrawPosition();
		
		paintLegend();
		paintDummyLines();
		paintLines();
		paintStations();
		paintVehicles();
	}
	
	private void paintLegend() {
		moveDraw(16, 16);
		for(VehicleType type : model.getVehicleTypes()) {
			g.setColor(type.getColor());
			point(0, 0, 6);
			moveDraw(16, 0);
			strToRight(type.getName(), 0, 0);
			moveDraw(-16, 16);
		}
		resetDrawPosition();
	}
	
	private void paintLines() {
		g.setColor(Color.GRAY);
		for(Line line : model.getLines()) {
			Station previous = null;
			for(Station current : line) {
				if(previous != null) {
					Position p1 = layout.getPosition(previous);
					Position p2 = layout.getPosition(current);
					
					ln(p1.x, p1.y, p2.x, p2.y);
				}
				previous = current;
			}
		}
	}
	
	private void paintDummyLines() {
		g.setColor(new Color(220, 220, 220));
		for(Station lastStation : model.getStations()) {
			if(!lastStation.isExitingStation()) {
				continue;
			}
			for(Line line : model.getLines()) {
				Station firstStation = line.getFirstStation();
				Position from = layout.getPosition(firstStation);
				Position to = layout.getPosition(lastStation);
				
				ln(from.x, from.y, to.x, to.y);
			}
		}
	}
	
	private void paintStations() {
		g.setColor(Color.BLACK);
		for(Station station : model.getStations()) {
			resetDrawPosition();
			Position p = layout.getPosition(station);
			
			if(station.isBoardingStation()) {
				point(p.x, p.y);
				count(p.x, p.y, station.getCurrentPeopleCount());
				moveDraw(0, -10);
				strBox(station.getName(), p.x, p.y);
			} else {
				point(p.x, p.y);
				strBox(station.getName(), p.x, p.y);
			}

		}
		resetDrawPosition();
	}
	
	private void paintVehicles() {
		for(Vehicle vehicle : new HashSet<Vehicle>(model.getVehicles())) {
			Station fromStation = vehicle.getStationGoingFrom();
			Station toStation = vehicle.getStationGoingTo();
			
			if(fromStation != null && toStation != null) {
				Position from = layout.getPosition(fromStation);
				Position to = layout.getPosition(toStation);
				Position position = i(from, to, vehicle.STATION_TRANSITION.getProgress(simulationTime));
				
				g.setColor(vehicle.getType().getColor());
				point(position.x, position.y, 6);
				progress(position.x, position.y, 10, 4, vehicle.getFullness());
			}
		}
	}
	
	@Override
	public void onClick(double x, double y) {
		for(Station station : model.getStations()) {
			if(!station.isBoardingStation()) {
				continue;
			}
			Position p = layout.getPosition(station);
			if(p != null) {
				if(isAt(x, y, p.x, p.y)) {
					stationCanvas.setStation(station);
				}
			}
		}
	}

	public Emitter<Double> getRepainter() {
		return repainter;
	}
}
