package agenti;

import entity.SkupinaZakaznikov;
import manazeri.ManazerModelu;
import simulacia.Id;
import simulacia.Mc;
import simulacia.SimulaciaRestovracie;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentModelu extends Agent
{	
	public AgentModelu(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);

		new ManazerModelu(Id.manazerModelu, mySim, this);
		
		addOwnMessage(Mc.prichodZakaznika);
		addOwnMessage(Mc.obsluhaZakaznikaUkoncena);
	}

	public int pocetZakaznikovVRestauracii()
	{
		int pocet = 0;
		
		try
		{
			for (SkupinaZakaznikov zakaznik : ((SimulaciaRestovracie)mySim()).agentJedalne().zakaznici())
			{
				pocet += zakaznik.pocet();
			}
			for (SkupinaZakaznikov zakaznik : ((SimulaciaRestovracie)mySim()).agentKuchyne().zakaznici())
			{
				pocet += zakaznik.pocet();
			}
		}
		catch (RuntimeException ex)
		{
			System.out.println(".");
		}
		return pocet;
	}
}
