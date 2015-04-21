package sk.nixone.ds.agent.poc;

import sk.nixone.ds.agent.SimulationRun;

public class Application {
	public static void main(String[] args) {
		SimulationRun simulation = new SimulationRun();
		Agent agent = new Agent(simulation);
		
		Message message = new Message(simulation);
		message.setCode(Messages.INIT);
		message.setAddressee(agent);
		agent.manager().notice(message);
		
		simulation.simulate();
		System.out.println("Finished");
	}
}
