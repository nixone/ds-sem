package riadiaciAgenti;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kontinualnyAsistenti.PlanovacOdchoduRoliek;
import promptnyAsistenti.DotazVyberDopravnik;
import riadiaciManazeri.ManazerDopravnikov;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import entity.Dopravnik;
import gui.RolkyDataSource;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentDopravnikov extends Agent implements RolkyDataSource
{
	// asistenti
	private DotazVyberDopravnik _dotazVyberDopravnik;
	private PlanovacOdchoduRoliek _planovacOdchoduRoliek;
	
	// entity
	private Map< Integer, Dopravnik > _dopravniky;

	public AgentDopravnikov(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		new ManazerDopravnikov(Id.manazerDopravnikov, mySim, this);
		
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.hold);
		addOwnMessage(Mc.pridanieRolkyDoDopravnika);
		addOwnMessage(Mc.odstranenieRolkyZDopravnika);
		
		_dotazVyberDopravnik = new DotazVyberDopravnik(Id.dotazVyberDopravnik, mySim, this);
		_planovacOdchoduRoliek = new PlanovacOdchoduRoliek(Id.planovacOdchoduRoliek, mySim, this);
		
		initDopravniky();
	}
	
	public DotazVyberDopravnik dotazVyberDopravnik()
	{ return _dotazVyberDopravnik; }
	
	public PlanovacOdchoduRoliek planovacOdchoduRoliek()
	{ return _planovacOdchoduRoliek; }

	private void initDopravniky()
	{
		int [] kapacita = { 10, 8, 13 };
		boolean [] jeVstupny = { true, true, false };
		int [] idDopravnika = { Id.dopravnik1, Id.dopravnik2, Id.dopravnik3 };
		
		_dopravniky = new HashMap<>(3);
		for (int i = 0; i < kapacita.length; ++i )
		{
			_dopravniky.put(idDopravnika[i], new Dopravnik(idDopravnika[i], mySim(), kapacita[i], jeVstupny[i]));
		}
	}
	
	public Map< Integer, Dopravnik > dopravniky()
	{ return _dopravniky; }
	
	public Dopravnik dopravnik(int idDopravniku)
	{
		return _dopravniky.get(idDopravniku);
	}

	@Override
	public List< Sprava > rolky()
	{
		List< Sprava > rolky = new LinkedList<>();
		
		for (Dopravnik dopravnik : _dopravniky.values())
		{
			try
			{
				rolky.addAll(dopravnik.rolky());
			}
			catch (RuntimeException ex) { }
		}
		return rolky;
	}
}
