package sk.nixone.ds.agent.sem3;

import sk.nixone.ds.agent.sem3.agents.BoardingAgent;
import sk.nixone.ds.agent.sem3.agents.BusMovementAgent;
import sk.nixone.ds.agent.sem3.agents.ModelAgent;
import sk.nixone.ds.agent.sem3.agents.SurroundingAgent;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;

public class SimulationRun extends sk.nixone.ds.agent.SimulationRun {
	
	private ModelAgent modelAgent;
	
	private BusMovementAgent busMovementAgent;
	
	private SurroundingAgent surroundingAgent;
	
	private BoardingAgent boardingAgent;
	
	public SimulationRun(Randoms randoms, Model model) {
		super();
		
		modelAgent = new ModelAgent(model, this);
		busMovementAgent = new BusMovementAgent(model, this, modelAgent);
		surroundingAgent = new SurroundingAgent(model, this, modelAgent);
		boardingAgent = new BoardingAgent(model, this, modelAgent);
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
	
	public BoardingAgent getBoardingAgent() {
		return boardingAgent;
	}
}
