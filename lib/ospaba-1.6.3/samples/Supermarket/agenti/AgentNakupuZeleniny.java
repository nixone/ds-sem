package supermarket.agenti;

import supermarket.asistenti.ProcesNakupuZeleniny;
import supermarket.manazeri.ManazerNakupuZeleniny;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentNakupuZeleniny extends AgentOddeleniaSPracovnikom
{	
	public AgentNakupuZeleniny(int id, Simulation mySim, Agent parent, int pocetPracovnikov)
	{
		super(id, mySim, parent, pocetPracovnikov);
	
		new ManazerNakupuZeleniny(Id.manazerZelenina, mySim, this);

		setProces(new ProcesNakupuZeleniny(Id.nakupZelenina, mySim, this));

		addOwnMessage(Mc.nakupZeleninu);
		addOwnMessage(Mc.procesNakupuZeleninyHotovy);
	}	
}
