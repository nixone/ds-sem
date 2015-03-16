package sk.nixone.ds.core.time;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class SimulationRun {

	private double currentSimulationTime = 0f;

	private PriorityQueue<PlannedEvent> eventCalendar;
	
	private boolean running = false;

	private Simulation simulation;
	
	private double maximumSimulationTime = 0;
	
	public SimulationRun(Simulation simulation) {
		eventCalendar = new PriorityQueue<>(10, PlannedEvent.TIME_COMPARATOR);
		this.simulation = simulation;
	}

	public double getCurrentSimulationTime() {
		return currentSimulationTime;
	}

	protected PlannedEvent planToTime(double timeToExecute, Event event) {
		PlannedEvent plannedEvent = new PlannedEvent(this, timeToExecute, currentSimulationTime, event);
		eventCalendar.add(plannedEvent);
		simulation.dispatchEventPlanned(this, plannedEvent);
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
	
	public void run() {
		run(new SimpleTimeJumper());
	}

	public void run(TimeJumper timeJumper) {
		running = true;
		while (running && !eventCalendar.isEmpty() && (maximumSimulationTime == 0 || currentSimulationTime < maximumSimulationTime)) {
			nextStep(timeJumper);
		}
		running = false;
	}

	public void nextStep() {
		nextStep(new SimpleTimeJumper());
	}
	
	public void nextStep(TimeJumper timeJumper) {
		PlannedEvent probablyNext = eventCalendar.peek();
		
		if (currentSimulationTime < probablyNext.getExecutionTime()) {
			double nextTime = maximumSimulationTime == 0 ? probablyNext.getExecutionTime() : Math.min(maximumSimulationTime, probablyNext.getExecutionTime());
			currentSimulationTime = timeJumper.jump(currentSimulationTime, nextTime);
		}
		
		if (currentSimulationTime == probablyNext.getExecutionTime()) {
			probablyNext = eventCalendar.poll();
			probablyNext.getEvent().execute(this);
			simulation.dispatchExecutedEvent(this, probablyNext);
		} else {
			simulation.dispatchVoidStep(this);
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}
	
	/**
	 * Casovo narocna operacia! Vrati zoradene prvky v kalendari udalosti.
	 * @return
	 */
	public List<PlannedEvent> getPlannedEvents() {
		PriorityQueue<PlannedEvent> calendarCopy = new PriorityQueue<>(10, PlannedEvent.TIME_COMPARATOR);
		calendarCopy.addAll(eventCalendar);
		
		ArrayList<PlannedEvent> orderedList = new ArrayList<PlannedEvent>(calendarCopy.size());
		while(!calendarCopy.isEmpty()) {
			orderedList.add(calendarCopy.poll());
		}
		return orderedList;
	}
}
