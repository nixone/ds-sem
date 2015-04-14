package supermarket.agenti;

import supermarket.asistenti.ProcesNakupuMasaASyru;
import supermarket.manazeri.ManazerNakupuMasoSyr;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentNakupuMasoSyr extends AgentOddeleniaSPracovnikom
{
	public AgentNakupuMasoSyr(int id, Simulation mySim, Agent parent, int pocetPracovnikov)
	{
		super(id, mySim, parent, pocetPracovnikov);
	
		new ManazerNakupuMasoSyr(Id.manazerMasoSyr, mySim, this);

		setProces(new ProcesNakupuMasaASyru(Id.nakupMasoSyr, mySim, this));

		addOwnMessage(Mc.nakupMasoSyr);
		addOwnMessage(Mc.procesNakupuMasaSyruHotovy);
	}
}
