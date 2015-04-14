package supermarket.simulacia;

import supermarket.agenti.AgentModelu;
import supermarket.agenti.AgentNakupu;
import supermarket.agenti.AgentNakupuMasoSyr;
import supermarket.agenti.AgentNakupuZeleniny;
import supermarket.agenti.AgentOkolia;
import supermarket.agenti.AgentPokladne;
import supermarket.agenti.AgentSupermarketu;
import OSPABA.Simulation;

public class SimulaciaSupermarketu extends Simulation
{
	private AgentModelu _agentModelu;
	private AgentOkolia _agentOkolia;
	private AgentSupermarketu _agentSupermarketu;
	private AgentNakupu _agentNakupu;
	private AgentNakupuZeleniny _agentNakupuZeleniny;
	private AgentNakupuMasoSyr _agentNakupuMasoSyr;
	private AgentPokladne _agentPokladne;
	
	private boolean _novySposobPrace;
	
	public SimulaciaSupermarketu(int pocetPracovnikovZelenina, int pocetPracovnikovMasoSyr, int pocetPracovnikovPokladne, boolean novySposobPrace)
	{
		_novySposobPrace = novySposobPrace;

		_agentModelu = new AgentModelu(Id.agentModelu, this, null);
		_agentOkolia = new AgentOkolia(Id.agentOkolia, this, _agentModelu);
		_agentSupermarketu = new AgentSupermarketu(Id.agentSupermarketu, this, _agentModelu);
		_agentNakupu = new AgentNakupu(Id.agentOstatneOddelenia, this, _agentSupermarketu);
		_agentNakupuZeleniny = new AgentNakupuZeleniny(Id.agentZelenina, this, _agentSupermarketu, pocetPracovnikovZelenina);
		_agentNakupuMasoSyr = new AgentNakupuMasoSyr(Id.agentMasoSyr, this, _agentSupermarketu, pocetPracovnikovMasoSyr);
		_agentPokladne = new AgentPokladne(Id.agentPokladna, this, _agentSupermarketu, pocetPracovnikovPokladne);
		
		if (_novySposobPrace)
		{
			_agentPokladne.nastavNovySposobPrace();
		}
	}

	public AgentModelu agentModelu()
	{ return _agentModelu; }

	public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public AgentSupermarketu agentSupermarketu()
	{ return _agentSupermarketu; }
	
	public AgentNakupu agentNakupu()
	{ return _agentNakupu; }
	
	public AgentNakupuZeleniny agentNakupuZeleniny()
	{ return _agentNakupuZeleniny; }
	
	public AgentNakupuMasoSyr agentNakupuMasoSyr()
	{ return _agentNakupuMasoSyr; }
	
	public AgentPokladne agentPokladne()
	{ return _agentPokladne; }
	
	public boolean novySposobPrace()
	{ return _novySposobPrace; }
		
	public int pocetVygenerovanychZakaznikov()
	{
		return _agentOkolia.pocetZakaznikov();
	}
	
	public int pocetZakaznikovVSysteme()
	{
		int pocet = _agentNakupu.pocetZakaznikov();
		pocet += _agentNakupuZeleniny.pocetZakaznikov();
		pocet += _agentNakupuMasoSyr.pocetZakaznikov();
		pocet += _agentPokladne.pocetZakaznikov();
		
		return pocet;
	}
	
	public int pocetObsluzenychZakaznikov()
	{	
		return _agentModelu.pocetObsluzenychZakaznikov();
	}
}
