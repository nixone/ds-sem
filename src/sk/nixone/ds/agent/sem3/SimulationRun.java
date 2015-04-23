package sk.nixone.ds.agent.sem3;

import java.util.HashMap;
import java.util.HashSet;

import sk.nixone.ds.agent.sem3.agents.BusMovementAgent;
import sk.nixone.ds.agent.sem3.agents.ModelAgent;
import sk.nixone.ds.agent.sem3.agents.SurroundingAgent;
import sk.nixone.ds.agent.sem3.model.Lines;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Stations;
import sk.nixone.ds.core.Randoms;

public class SimulationRun extends sk.nixone.ds.agent.SimulationRun {
	
	private ModelAgent modelAgent;
	
	private BusMovementAgent busMovementAgent;
	
	private SurroundingAgent surroundingAgent;
	
	public SimulationRun(Randoms randoms, Model model) {
		super();
		
		modelAgent = new ModelAgent(randoms, model, this);
		busMovementAgent = new BusMovementAgent(randoms, model, this, modelAgent);
		surroundingAgent = new SurroundingAgent(randoms, model, this, modelAgent);
	}
	
	public ModelAgent getModelAgent() {
		return modelAgent;
	}
	
	public BusMovementAgent getBusMovementAgent() {
		return busMovementAgent;
	}
	
	public SurroundingAgent getSurroundingAgent() {
		return surroundingAgent;
	}
}
