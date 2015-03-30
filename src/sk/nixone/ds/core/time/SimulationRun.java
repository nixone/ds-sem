package sk.nixone.ds.core.time;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

public class SimulationRun {

	protected HashSet<ProcessMarker> ongoingMarkers = new HashSet<ProcessMarker>();
	
	private double currentSimulationTime = 0f;

	private PriorityQueue<PlannedEvent> eventCalendar;
	
	private boolean running = false;

	private Simulation simulation;
	
	private double maximumSimulationTime = 0;
	
	public SimulationRun(Simulation simulation) {
		eventCalendar = new PriorityQueue<>(10, PlannedEvent.TIME_COMPARATOR);
		this.simulation = simulation;
	}

	public void setCurrentSimulationTime(double simulationTime) {
		currentSimulationTime = simulationTime;
	}
	
	public double getCurrentSimulationTime() {
		return currentSimulationTime;
	}

	protected PlannedEvent planToTime(double timeToExecute, Event event) {
		PlannedEvent plannedEvent = new PlannedEvent(this, timeToExecute, currentSimulationTime, event);
		eventCalendar.add(plannedEvent);
		return plannedEvent;
	}
	
	public PlannedEvent planImmediately(Event event) {
		return planToTime(currentSimulationTime, event);
	}
	
	public PlannedEvent plan(double timeFromNow, Event event) {
		return planToTime(timeFromNow + currentSimulationTime, event);
	}

	public void setMaximumSimulationTime(double maximumSimulationTime) {
		this.maximumSimulationTime = maximumSimulationTime;
	}

	public void run(SimulationConfig config) {
		running = true;
		while (running && !eventCalendar.isEmpty() && (maximumSimulationTime == 0 || currentSimulationTime < maximumSimulationTime)) {
			nextStep(config);
		}
		running = false;
	}
	
	public void nextStep(SimulationConfig config) {
		PlannedEvent probablyNext = eventCalendar.peek();
		
		if (currentSimulationTime < probablyNext.getExecutionTime()) {
			double nextTime = maximumSimulationTime == 0 ? probablyNext.getExecutionTime() : Math.min(maximumSimulationTime, probablyNext.getExecutionTime());
			currentSimulationTime = config.getJumper().jump(currentSimulationTime, nextTime);
		}
		
		if (currentSimulationTime == probablyNext.getExecutionTime()) {
			probablyNext = eventCalendar.poll();
			probablyNext.getEvent().execute(this);
		}
		
		if (!config.isIgnoreRunImmediateEmitters()) {
			simulation.dispatchSimulationUpdated();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}
	
	public void replanInto(SimulationRun newRun, double modelDuration) {
		Iterator<PlannedEvent> it = eventCalendar.iterator();
		while(it.hasNext()) {
			PlannedEvent event = it.next();
			newRun.plan(event.getExecutionTime()-modelDuration, event.getEvent());
		}
		for(ProcessMarker marker : ongoingMarkers) {
			marker.moveStartTime(-modelDuration);
		}
	}
}
