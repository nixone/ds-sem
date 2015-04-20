package sk.nixone.ds.agent;

public class Agent<T extends SimulationRun> extends OSPABA.Agent {

	public Agent(int id, T simulation, Agent<T> parent) {
		super(id, simulation, parent);
	}

	public T getSimulation() {
		return (T)mySim();
	}
}
