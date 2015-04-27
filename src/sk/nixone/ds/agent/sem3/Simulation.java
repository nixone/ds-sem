package sk.nixone.ds.agent.sem3;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;

public class Simulation extends sk.nixone.ds.agent.Simulation {
	
	private Model model;
	
	private Randoms randoms;
	
	public Simulation(Randoms randoms, Model model) {
		super();
		this.randoms = randoms;
		this.model = model;
	}
	
	@Override
	public void onStarted() {
	}

	@Override
	public void onEnded() {
	}

	@Override
	public void onReplicationStart(int replicationIndex) {
	}

	@Override
	public void onReplicationEnd(int replicationIndex) {
	}
	
	protected SimulationRun createSimulationRun() {
		model.reset();
		
		SimulationRun run = new SimulationRun(randoms, model);
		
		run.stopSimulation(model.getMatchStartTime());
		
		Message message = new Message(run);
		message.setCode(Messages.INIT);
		Agent<?> modelAgent = (Agent<?>)run.findAgent(Components.A_MODEL);
		message.setAddressee(modelAgent);
		modelAgent.manager().notice(message);
		
		return run;
	}
}
