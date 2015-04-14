package riadiaciAgenti;

import riadiaciManazeri.ManazerOceliarne;
import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentOceliarne extends Agent
{
	public AgentOceliarne(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		new ManazerOceliarne(Id.manazerOceliarne, mySim, this);

		addOwnMessage(Mc.init);
		addOwnMessage(Mc.initRolkaSkladu);
		addOwnMessage(Mc.spracovanieRolky);
		
		addOwnMessage(Mc.pridelenieMiestaVSkladeDokoncene);
		addOwnMessage(Mc.opracovanieRolkyDokoncene);
		
		addOwnMessage(Mc.pridelenieVozidlaDokoncene);
		addOwnMessage(Mc.presunVozidlaDokonceny);
		
		addOwnMessage(Mc.pridelenieZeriavuDokoncene);
		addOwnMessage(Mc.prelozenieRolkyDokoncene);

		addOwnMessage(Mc.pridanieRolkyDoDopravnikaDokoncene);
		addOwnMessage(Mc.odstranenieRolkyZDopravnikaDokoncene);
		
		addOwnMessage(Mc.odchodRolky);
	}
}
