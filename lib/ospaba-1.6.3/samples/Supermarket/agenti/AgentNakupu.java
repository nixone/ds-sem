package supermarket.agenti;

import java.util.List;

import supermarket.asistenti.ProcesNakupu;
import supermarket.entity.Zakaznik;
import supermarket.manazeri.ManazerNakupu;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.Simulation;

public class AgentNakupu extends Agent
{
	private ContinualAssistant _procesNakupu;

	public AgentNakupu(int id, Simulation mySim, Agent parent) 
	{
		super(id, mySim, parent);
		
		new ManazerNakupu(Id.manazerOstatneOddelenia, mySim, this);
		
		_procesNakupu = new ProcesNakupu(Id.nakup, mySim, this);
		
		addOwnMessage(Mc.nakupOstatne);
		addOwnMessage(Mc.procesNakupuOstatneHotovy);
	}
	
	public ContinualAssistant procesNakupu()
	{ return _procesNakupu; }
	
	public List<Zakaznik> zakaznici()
	{ return ((ManazerNakupu)manager()).zakaznici(); }
	
	public int pocetZakaznikov()
	{ return ((ManazerNakupu)manager()).zakaznici().size(); }
	
//	@Override
//	public void reset()
//	{
//		((ManazerNakupu)manager()).zakaznici().clear();
//	}
}
