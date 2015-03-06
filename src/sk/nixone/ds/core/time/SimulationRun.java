package sk.nixone.ds.core.time;

import java.util.PriorityQueue;

public class SimulationRun {

	private float currentSimulationTime = 0f;

	private PriorityQueue<PlannedEvent> eventCalendar;

	private boolean running = false;

	public SimulationRun() {
		eventCalendar = new PriorityQueue<PlannedEvent>(PlannedEvent.TIME_COMPARATOR);
	}

	public float getCurrentSimulationTime() {
		return currentSimulationTime;
	}

	public PlannedEvent plan(float timeFromNow, Event event) {
		PlannedEvent plannedEvent = new PlannedEvent(this, currentSimulationTime + timeFromNow, currentSimulationTime, event);
		eventCalendar.add(plannedEvent);
		return plannedEvent;
	}

	public void run() {
		run(new SimpleTimeJumper());
	}

	public void run(TimeJumper timeJumper) {
		running = true;
		while (running) {
			nextStep(timeJumper);
		}
	}

	public void nextStep(TimeJumper timeJumper) {
		PlannedEvent probablyNext = eventCalendar.peek();
		
		if (currentSimulationTime < probablyNext.getExecutionTime()) {
			currentSimulationTime = timeJumper.jump(currentSimulationTime, probablyNext.getExecutionTime());
		} else {
			eventCalendar.poll().getEvent().execute();
		}
    }

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
	}
}
