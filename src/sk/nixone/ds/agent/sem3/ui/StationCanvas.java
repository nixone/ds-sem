package sk.nixone.ds.agent.sem3.ui;

import java.util.ArrayList;

import sk.nixone.ds.agent.sem3.model.Person;
import sk.nixone.ds.agent.sem3.model.Station;
import sk.nixone.ds.agent.sem3.model.Vehicle;
import sk.nixone.ds.agent.sem3.model.Vehicle.Door;
import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;
import sk.nixone.util.TimeUtil;

public class StationCanvas extends HelperCanvas {

	private Emitter<Double> refresher = new DelayedEmitter<Double>(new Emitter<Double>() {

		@Override
		public void reset() {
			displaying = false;
			station = null;
		}

		@Override
		public void emit(Double value) {
			displaying = station != null;
			currentTime = value;
			repaint();
		}
	
	}, 15);
	
	private Station station = null;
	private double currentTime = 0;	
	
	@Override
	public void paintDisplay() {
		if(station == null) {
			return;
		}
		strBig("Station: "+station.getName(), 0.5, 0.05);
		str("People waiting:", 0.85, 0.3);
		strBig(String.valueOf(station.getCurrentPeopleCount()), 0.85, 0.35);
		
		Person firstPerson = station.peekFirstPerson();
		if(firstPerson != null) {
			str("First waiting for:", 0.85, 0.45);
			strBig(TimeUtil.toString((int)(currentTime - firstPerson.WAITING_FOR_BUS.getStartTime())), 0.85, 0.5);
		}
		
		paintVehicles();
	}
	
	private void paintVehicles() {
		resetPosition();
		strBig("Vehicles", 0.2, 0.1);
		move(0.2, 0.2);
		
		for(Vehicle vehicle : new ArrayList<Vehicle>(station.getVehicles())) {
			resetDrawPosition();
			g.setColor(vehicle.getType().getColor());
			point(0, 0, 20);
			progress(0, 0, 100, 30, vehicle.getFullness());
			str(vehicle.getType().getName()+" ("+vehicle.getPeopleCount()+" / "+vehicle.getType().getCapacity()+")", 0, 0);
			
			if(vehicle.WAITING_FOR_ARRIVALS.didHappen() && !vehicle.WAITING_FOR_ARRIVALS.didFinish()) {
				moveDraw(0, 25);
				progress(0, 0, 100, 15, vehicle.WAITING_FOR_ARRIVALS.getProgress(currentTime));
				str("Waiting...", 0, 0);
				moveDraw(0, -25);
			}
			
			moveDraw(70, 0);
			for(Door door : vehicle.getDoors()) {
				progress(0, 0, 20, 30, door.USAGE.getProgress(currentTime));
				moveDraw(30, 0);
			}
			
			move(0, 0.2);
		}
		resetPosition();
		resetDrawPosition();
	}
	
	public Emitter<Double> getEmitter() {
		return refresher;
	}
	
	public void setStation(Station station) {
		this.station = station;
		displaying = station != null;
		repaint();
	}
	
	public Emitter<Double> getRefresher() {
		return refresher;
	}
}
