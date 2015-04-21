package sk.nixone.ds.agent.poc;

import sk.nixone.ds.agent.SimulationRun;

public class Agent extends sk.nixone.ds.agent.Agent<SimulationRun> {

	public Agent(SimulationRun mySim) {
		super(Components.AGENT, mySim, null);
		
		new Manager(mySim, this);
		new Assistant(mySim, this);
		
		addOwnMessage(Messages.INIT);
		addOwnMessage(Messages.WORK);
	}
}
