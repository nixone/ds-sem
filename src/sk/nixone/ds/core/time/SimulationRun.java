package sk.nixone.ds.core.time;

import java.util.HashSet;
import java.util.PriorityQueue;

public class SimulationRun {
	
	public interface Observer {
		public void onEventPlanned(SimulationRun run, PlannedEvent event);
		public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent);
		public void onVoidStep(SimulationRun run);
	}

	private double currentSimulationTime = 0f;

	private PriorityQueue<PlannedEvent> eventCalendar;
	private HashSet<Observer> observers;
	
	private boolean running = false;

	public SimulationRun() {
		eventCalendar = new PriorityQueue<>(10, PlannedEvent.TIME_COMPARATOR);
		observers = new HashSet<>();
	}

	public double getCurrentSimulationTime() {
		return currentSimulationTime;
	}

	protected PlannedEvent planToTime(double timeToExecute, Event event) {
		PlannedEvent plannedEvent = new PlannedEvent(this, timeToExecute, currentSimulationTime, event);
		eventCalendar.add(plannedEvent);
		dispatchEventPlanned(plannedEvent);
		return plannedEvent;
	}
	
	public PlannedEvent planImmediately(Event event) {
		return planToTime(currentSimulationTime, event);
	}
	
	public PlannedEvent plan(double timeFromNow, Event event) {
		return planToTime(timeFromNow + currentSimulationTime, event);
	}

	public void run() {
		run(new SimpleTimeJumper());
	}

	public void run(TimeJumper timeJumper) {
		running = true;
		while (running && !eventCalendar.isEmpty()) {
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
			currentSimulationTime = timeJumper.jump(currentSimulationTime, probablyNext.getExecutionTime());
		}
		
		if (currentSimulationTime == probablyNext.getExecutionTime()) {
			probablyNext = eventCalendar.poll();
			probablyNext.getEvent().execute();
			dispatchExecutedEvent(probablyNext);
		} else {
			dispatchVoidStep();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}
	
	public void addObserver(Observer observer) {
		synchronized(observers) {
			observers.add(observer);
		}
	}
	
	public void removeObserver(Observer observer) {
		synchronized(observers) {
			observers.remove(observer);
		}
	}
	
	private HashSet<Observer> copyObservers() {
		HashSet<Observer> observersCopy = new HashSet<>();
		
		synchronized(observers) {
			observersCopy.addAll(observers);
		}
		
		return observersCopy;
	}
	
	private void dispatchExecutedEvent(PlannedEvent event) {
		for(Observer observer : copyObservers()) {
			observer.onExecutedEvent(this, event);
		}
	}
	
	private void dispatchVoidStep() {
		for(Observer observer : copyObservers()) {
			observer.onVoidStep(this);
		}
	}
	
	private void dispatchEventPlanned(PlannedEvent event) {
		for(Observer observer : copyObservers()) {
			observer.onEventPlanned(this, event);
		}
	}
}
