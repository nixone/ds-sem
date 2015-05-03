package sk.nixone.ds.agent.sem3;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import sk.nixone.ds.agent.sem3.agents.BoardingAgent;
import sk.nixone.ds.agent.sem3.agents.MovementAgent;
import sk.nixone.ds.agent.sem3.agents.ExitingAgent;
import sk.nixone.ds.agent.sem3.agents.ModelAgent;
import sk.nixone.ds.agent.sem3.agents.SurroundingAgent;
import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.SequenceStatistic;

public class SimulationRun extends sk.nixone.ds.agent.SimulationRun {
	
	private ModelAgent modelAgent;
	
	private MovementAgent busMovementAgent;
	
	private SurroundingAgent surroundingAgent;
	
	private BoardingAgent boardingAgent;
	
	private ExitingAgent exitingAgent;
	
	private int totalPeople = 0;
	
	private int servedPeople = 0;
	
	private HashMap<Line, AtomicInteger> totalPeopleOnLines = new HashMap<Line, AtomicInteger>();
	
	private HashMap<Line, AtomicInteger> servedPeopleOnLines = new HashMap<Line, AtomicInteger>();
	
	private SequenceStatistic personWaitingTime = new SequenceStatistic();
	
	private SequenceStatistic busFullnessStatistic = new SequenceStatistic();
	
	private int gained = 0;
	
	public SimulationRun(Randoms randoms, Model model) {
		super();
		
		for(Line line : model.getLines()) {
			totalPeopleOnLines.put(line, new AtomicInteger(0));
			servedPeopleOnLines.put(line, new AtomicInteger(0));
		}
		
		modelAgent = new ModelAgent(model, this);
		busMovementAgent = new MovementAgent(model, this, modelAgent);
		surroundingAgent = new SurroundingAgent(model, this, modelAgent);
		boardingAgent = new BoardingAgent(model, this, modelAgent);
		exitingAgent = new ExitingAgent(model, this, modelAgent);
	}
	
	public SequenceStatistic getPersonWaitingTime() {
		return personWaitingTime;
	}
	
	public SequenceStatistic getBusFullnessStatistic() {
		return busFullnessStatistic;
	}
	
	public void increaseGain(int by) {
		gained += by;
	}
	
	public int getGained() {
		return gained;
	}
	
	public void increaseTotalPeople() {
		totalPeople++;
	}
	
	public void increaseTotalPeople(Line line) {
		totalPeopleOnLines.get(line).incrementAndGet();
	}
	
	public void increaseServedPeople(int by) {
		servedPeople += by;
	}
	
	public void increaseServedPeople(Line line, int by) {
		servedPeopleOnLines.get(line).addAndGet(by);
	}
	
	public int getTotalPeople() {
		return totalPeople;
	}
	
	public int getServedPeople() {
		return servedPeople;
	}
	
	public int getTotalPeople(Line line) {
		return totalPeopleOnLines.get(line).get();
	}
	
	public int getServedPeople(Line line) {
		return servedPeopleOnLines.get(line).get();
	}
	
	public ModelAgent getModelAgent() {
		return modelAgent;
	}
	
	public MovementAgent getBusMovementAgent() {
		return busMovementAgent;
	}
	
	public SurroundingAgent getSurroundingAgent() {
		return surroundingAgent;
	}
	
	public BoardingAgent getBoardingAgent() {
		return boardingAgent;
	}
	
	public ExitingAgent getExitingAgent() {
		return exitingAgent;
	}
}
