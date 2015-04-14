package supermarket.agenti;

import supermarket.asistenti.PlanovacPrichoduZakaznika;
import supermarket.manazeri.ManazerOkolia;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentOkolia extends Agent
{	
	private PlanovacPrichoduZakaznika _planovacPrichoduZakaznika;
	private int _pocetZakaznikov;

	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		
		new ManazerOkolia(Id.manazerOkolia, mySim, this);

		_planovacPrichoduZakaznika = new PlanovacPrichoduZakaznika(Id.prichodZakaznika, mySim, this);
		
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.novyZakaznik);
	}
	
	public PlanovacPrichoduZakaznika planovacPrichoduZakaznika()
	{ return _planovacPrichoduZakaznika; }

	public int pocetZakaznikov()
	{ return _pocetZakaznikov; }

	public void incPocetZakaznikov()
	{ ++_pocetZakaznikov; }	
}
