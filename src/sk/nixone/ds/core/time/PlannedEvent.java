package sk.nixone.ds.core.time;

import java.util.Comparator;

/**
 * Result of planning a certain <code>Event</code> in <code>SimulationRun</code>.
 * This can be used to count duration or replan itself to another <code>SimulationRun</code>.
 * 
 * @author nixone
 *
 */
public class PlannedEvent {
    
	/**
	 * Comparator that can be used to order the events from the earliest to the latest.
	 */
    static public Comparator<PlannedEvent> TIME_COMPARATOR = new Comparator<PlannedEvent>() {
	
		@Override
		public int compare(PlannedEvent o1, PlannedEvent o2) {
		    return Double.compare(o1.getExecutionTime(), o2.getExecutionTime());
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
    
    /**
     * The event that should be executed.
     * 
     * @return event that should be executed
     */
    public Event getEvent() {
		return event;
    }
    
    /**
     * Simulation run, in which the event was planned.
     * 
     * @return simulation run, in which the event was planned
     */
    public SimulationRun getSimulation() {
		return simulation;
    }
    
    /**
     * Time of simulation run, in which the event was planned.
     * @return time of simulation run, in which the event was planned
     */
    public double getScheduledTime() {
		return scheduledTime;
    }
    
    /**
     * Time of simulation run, in which the event should be executed.
     * @return time of simulation run, in which the event should be executed
     */
    public double getExecutionTime() {
		return executionTime;
    }
    
    /**
     * Determines, whether the event was already finished.
     * If the simulation run is still at the exact point in time in which this
     * event should be executing, it is still not considered finished
     * 
     * @return whether the event was surely executed
     */
    public boolean isFinished() {
		return simulation.getCurrentSimulationTime() > executionTime;
    }
    
    /**
     * Returns the approximation of "progress" of the current event.
     * Counts the linear ratio of current time in respect to the scheduled and execution time.
     * 
     * @return progress estimation
     */
    public double getProgress() {
		if (isFinished()) {
			return 1.;
		}

		double simulationTime = simulation.getCurrentSimulationTime();

		return (simulationTime - scheduledTime) / (executionTime - scheduledTime);
    }
}
