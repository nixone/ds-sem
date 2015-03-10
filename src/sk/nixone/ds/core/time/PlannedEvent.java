package sk.nixone.ds.core.time;

import java.util.Comparator;

public class PlannedEvent {
    
    static public Comparator<PlannedEvent> TIME_COMPARATOR = new Comparator<PlannedEvent>() {
	
	@Override
	public int compare(PlannedEvent o1, PlannedEvent o2) {
	    return Double.compare(o1.getScheduledTime(), o2.getScheduledTime());
	}
    };
    
    private Event event;
    private SimulationRun simulation;
    private double executionTime;
    private double scheduledTime;
    
    protected PlannedEvent(SimulationRun simulation, double executionTime, double scheduledTime, Event event) {
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
    
    public double getScheduledTime() {
		return scheduledTime;
    }
    
    public double getExecutionTime() {
		return executionTime;
    }
    
    public boolean isFinished() {
		return simulation.getCurrentSimulationTime() > executionTime;
    }
    
    public double getProgress() {
		if (isFinished()) {
			return 1.;
		}

		double simulationTime = simulation.getCurrentSimulationTime();

		return (simulationTime - scheduledTime) / (executionTime - scheduledTime);
    }
}
