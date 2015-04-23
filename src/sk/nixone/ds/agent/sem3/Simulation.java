package sk.nixone.ds.agent.sem3;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;

public class Simulation extends sk.nixone.ds.agent.Simulation {
	
	private Model input;
	
	private Randoms randoms;
	
	public Simulation(Randoms randoms, Model input) {
		super();
		this.randoms = randoms;
		this.input = input;
	}
	
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
		SimulationRun run = new SimulationRun(randoms, input);
		Message message = new Message(run);
		message.setCode(Messages.INIT);
		Agent<?> modelAgent = (Agent<?>)run.findAgent(Components.A_MODEL);
		message.setAddressee(modelAgent);
		modelAgent.manager().notice(message);
		return run;
	}
}
