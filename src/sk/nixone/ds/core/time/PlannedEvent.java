package sk.nixone.ds.core.time;

import java.util.Comparator;

public class PlannedEvent {
    
    static public Comparator<PlannedEvent> TIME_COMPARATOR = new Comparator<PlannedEvent>() {
	
	@Override
	public int compare(PlannedEvent o1, PlannedEvent o2) {
	    return Float.compare(o1.getScheduledTime(), o2.getScheduledTime());
	}
    };
    
    private Event event;
    private SimulationRun simulation;
    private float executionTime;
    private float scheduledTime;
    
    protected PlannedEvent(SimulationRun simulation, float executionTime, float scheduledTime, Event event) {
	this.scheduledTime = scheduledTime;
	this.executionTime = executionTime;
	this.event = event;
	this.simulation = simulation;
    }
    
    public Event getEvent() {
	return event;
    }
    
    public SimulationRun getSimulation() {
	return simulation;
    }
    
    public float getScheduledTime() {
	return scheduledTime;
    }
    
    public float getExecutionTime() {
	return executionTime;
    }
    
    public boolean isFinished() {
	return simulation.getCurrentSimulationTime() > executionTime;
    }
    
    public float getProgress() {
	if (isFinished()) {
	    return 1f;
	}
	
	float simulationTime = simulation.getCurrentSimulationTime();
	
	return (simulationTime - scheduledTime) / (executionTime - scheduledTime);
    }
}
