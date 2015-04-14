package supermarket.agenti;

import supermarket.manazeri.ManazerSupermarketu;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentSupermarketu extends Agent
{
	public AgentSupermarketu(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		
		new ManazerSupermarketu(Id.manazerSupermarketu, mySim, this);
		
		addOwnMessage(Mc.obsluzZakaznika);
		addOwnMessage(Mc.nakupOstatneHotovy);
		addOwnMessage(Mc.nakupZeleninyHotovy);
		addOwnMessage(Mc.nakupMasoSyrHotovy);
		addOwnMessage(Mc.pladbaHotova);
	}
}
