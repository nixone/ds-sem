package main;

import simulacia.Mc;
import simulacia.SimulaciaCerpacejStanice;
import simulacia.Sprava;

public class Main
{
	public static void main(String [] args)
	{
		SimulaciaCerpacejStanice sim = new SimulaciaCerpacejStanice();
		
		System.out.println("Simulating...");
		
		Sprava message = new Sprava(sim);
		message.setCode(Mc.init);
		message.setAddressee(sim.agentModelu());
		sim.agentModelu().manager().notice(message);
	
		sim.simulate(1000000d);
		
		System.out.println("Waiting time mean:  " + sim.agentCerpacejStanice().casCakania().mean());
		System.out.println("Queue length mean:  " + sim.agentCerpacejStanice().dlzkaFrontu().mean());
	}
}
