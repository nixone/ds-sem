package supermarket.agenti;

import supermarket.asistenti.ProcesPlatenia;
import supermarket.manazeri.ManazerPokladne;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentPokladne extends AgentOddeleniaSPracovnikom
{
	public AgentPokladne(int id, Simulation mySim, Agent parent, int pocetPracovnikov)
	{
		super(id, mySim, parent, pocetPracovnikov);
	
		new ManazerPokladne(Id.manazerPokladna, mySim, this);

		setProces(new ProcesPlatenia(Id.pladba, mySim, this));

		addOwnMessage(Mc.platenie);
		addOwnMessage(Mc.procesPlateniaHotovy);
	}
}
