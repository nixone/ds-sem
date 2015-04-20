package sk.nixone.ds.agent.sem3;

import sk.nixone.ds.agent.SimulationRun;

public class Simulation extends sk.nixone.ds.agent.Simulation {
	
	@Override
	public void onStarted() {
		System.out.println("Simulation started");
	}

	@Override
	public void onEnded() {
		System.out.println("Simulation ended");
	}

	@Override
	public void onReplicationStart(int replicationIndex) {
		System.out.println("Replication "+replicationIndex+" started");
	}

	@Override
	public void onReplicationEnd(int replicationIndex) {
		System.out.println("Replication "+replicationIndex+" ended");
	}
	
	protected SimulationRun createSimulationRun() {
		SimulationRun run = new SimulationRun();
		return run;
	}
}
