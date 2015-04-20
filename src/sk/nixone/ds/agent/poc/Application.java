package sk.nixone.ds.agent.poc;

import OSPABA.Simulation;

public class Application {
	public static void main(String[] args) {
		Simulation simulation = new Simulation();
		Agent agent = new Agent(simulation);
		
		Message message = new Message(simulation);
		message.setCode(Messages.INIT);
		message.setAddressee(agent);
		agent.manager().notice(message);
		
		simulation.simulate();
		System.out.println("Finished");
	}
}
