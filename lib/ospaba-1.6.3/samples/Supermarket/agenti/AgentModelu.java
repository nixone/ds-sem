package supermarket.agenti;

import java.util.List;

import supermarket.entity.Zakaznik;
import supermarket.manazeri.ManazerModelu;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;
import OSPStat.Stat;
import OSPStat.WStat;

public class AgentModelu extends Agent
{
	private Stat _statistikaCelkovyCasCakania;
	private WStat _statistikaDlzkaFrontov;
	
	public AgentModelu(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		
		new ManazerModelu(Id.manazerModelu, mySim, this);

		addOwnMessage(Mc.init);
		addOwnMessage(Mc.prichodZakaznika);
		addOwnMessage(Mc.odchodZakaznika);
		
		_statistikaCelkovyCasCakania = new Stat();
		_statistikaDlzkaFrontov = new WStat(mySim());
	}
	
//	@Override
//	public void reset()
//	{
//		super.reset();
//		_statistikaCelkovyCasCakania.reset();
//		_statistikaDlzkaFrontov.reset();
//		((ManazerModelu)manager()).obsluzenyZakaznici().clear();
//	}
	
	public List<Zakaznik> obsluzenyZakaznici()
	{
		return ((ManazerModelu)manager()).obsluzenyZakaznici();
	}
	
	public int pocetObsluzenychZakaznikov()
	{ return ((ManazerModelu)manager()).obsluzenyZakaznici().size(); }
	
	public Stat statistikaCelkovyCasCakania()
	{ return _statistikaCelkovyCasCakania; }
	
	public WStat statistikaDlzkaFrontov()
	{ return _statistikaDlzkaFrontov; }
}
