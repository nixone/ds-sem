package agenti;

import manazeri.ManazerModelu;
import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentModelu extends Agent
{
	public AgentModelu(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		
		new ManazerModelu(Id.manazerModelu, mySim, this);
		
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.prichodZakaznika);
		addOwnMessage(Mc.obsluhaZakaznikaHotova);
	}
}
