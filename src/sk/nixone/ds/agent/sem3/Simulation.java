package sk.nixone.ds.agent.sem3;

import sk.nixone.ds.agent.Agent;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.SequenceStatistic;

public class Simulation extends sk.nixone.ds.agent.Simulation {
	
	private Model model;
	
	private Randoms randoms;
	
	private SequenceStatistic latePeopleStatistic = new SequenceStatistic();
	private SequenceStatistic personWaitingTimeStatistic = new SequenceStatistic();
	
	private SimulationRun simulationRun;
	
	public Simulation(Randoms randoms, Model model) {
		super();
		this.randoms = randoms;
		this.model = model;
	}
	
	@Override
	public void onStarted() {
		latePeopleStatistic.clear();
		personWaitingTimeStatistic.clear();
	}

	@Override
	public void onEnded() {
	}

	@Override
	public void onReplicationStart(int replicationIndex) {
		
	}

	@Override
	public void onReplicationEnd(int replicationIndex) {
		latePeopleStatistic.add(1.-((double)simulationRun.getServedPeople() / simulationRun.getTotalPeople()));
		personWaitingTimeStatistic.add(simulationRun.getPersonWaitingTime().getMean());
	}
	
	public SequenceStatistic getLatePeopleStatistic() {
		return latePeopleStatistic;
	}
	
	public SequenceStatistic getPersonWaitingTimeStatistic() {
		return personWaitingTimeStatistic;
	}
	
	protected SimulationRun createSimulationRun() {
		model.reset();
		
		simulationRun = new SimulationRun(randoms, model);
		
		simulationRun.stopSimulation(model.getMatchStartTime());
		
		Message message = new Message(simulationRun);
		message.setCode(Messages.INIT);
		Agent<?> modelAgent = (Agent<?>)simulationRun.findAgent(Components.A_MODEL);
		message.setAddressee(modelAgent);
		modelAgent.manager().notice(message);
		
		return simulationRun;
	}
}
