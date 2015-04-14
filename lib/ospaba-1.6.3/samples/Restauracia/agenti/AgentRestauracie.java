package agenti;

import manazeri.ManazerRestauracie;
import simulacia.Id;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentRestauracie extends Agent
{	
	public AgentRestauracie(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);

		new ManazerRestauracie(Id.manazerRestovracie, mySim, this);

//		addOwnMessage(Mc.obsluhaZakaznika);
//		addOwnMessage(Mc.obsluhaZakaznikaUkoncena);
//		addOwnMessage(Mc.pripravaJedla);
//		addOwnMessage(Mc.pripravaJedlaUkoncena);
	}
}
