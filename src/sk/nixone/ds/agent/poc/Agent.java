package sk.nixone.ds.agent.poc;

import OSPABA.Simulation;

public class Agent extends OSPABA.Agent {

	public Agent(Simulation mySim) {
		super(Components.AGENT, mySim, null);
		
		new Manager(mySim, this);
		new Assistant(mySim, this);
		
		addOwnMessage(Messages.INIT);
		addOwnMessage(Messages.WORK);
	}
}
