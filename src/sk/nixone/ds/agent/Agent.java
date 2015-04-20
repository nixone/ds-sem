package sk.nixone.ds.agent;

import java.util.HashSet;

public class Agent<T extends SimulationRun> extends OSPABA.Agent {

	private HashSet<Integer> codes = new HashSet<Integer>();
	
	public Agent(int id, T simulation, Agent<T> parent) {
		super(id, simulation, parent);
	}

	public T getSimulation() {
		return (T)mySim();
	}
	
	@Override
	public void addOwnMessage(int code) {
		if (!codes.contains(code)) {
			codes.add(code);
			super.addOwnMessage(code);
		}
	}
}
