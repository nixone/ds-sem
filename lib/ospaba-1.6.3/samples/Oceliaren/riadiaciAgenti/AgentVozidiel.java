package riadiaciAgenti;

import gui.RolkyDataSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import promptnyAsistenti.DotazVolneVozidlo;
import promptnyAsistenti.PravidloVstupnychRoliek;
import dynamickyAgenti.AgentVozidla;
import riadiaciManazeri.ManazerVozidiel;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;

public class AgentVozidiel extends Agent implements RolkyDataSource
{
	private static final double kRychlostStarychVozidiel = 2.9 * 1000 / 60; // [m/min]
	private static final double kRychlostNovychVozidiel = 4.5 * 1000 / 60; // [m/min]
	public static final int kStartId = 10001; // id prveho vozidla

	private List< AgentVozidla > _vozidla;
	
	private SimQueue< Sprava > _rolkyCakajuceNaVozidlo_S4;
	private SimQueue< Sprava > _rolkyCakajuceNaVozidlo_S3;
	
	private DotazVolneVozidlo _dotazVolneVozidlo;
	private PravidloVstupnychRoliek _pravidloVstupnychRoliek;

	public AgentVozidiel(int id, Simulation mySim, Agent parent, int pocetStarychVozidiel, int pocetNovychVozidiel)
	{
		super(id, mySim, parent);
		new ManazerVozidiel(Id.manazerVozidiel, mySim, this);
		
		_rolkyCakajuceNaVozidlo_S4 = new SimQueue<>();
		_rolkyCakajuceNaVozidlo_S3 = new SimQueue<>();

		_dotazVolneVozidlo = new DotazVolneVozidlo(Id.dotazVolneVozidlo, mySim, this);
		_pravidloVstupnychRoliek = new PravidloVstupnychRoliek(Id.pravidloVstupnychRoliek, mySim, this, _rolkyCakajuceNaVozidlo_S4, _rolkyCakajuceNaVozidlo_S3);
		
		addOwnMessage(Mc.pridelenieVozidla);
		addOwnMessage(Mc.presunVozidla);
		addOwnMessage(Mc.uvolnenieVozidla);
		
		addOwnMessage(Mc.hold);
		
		_vozidla = new ArrayList<>(pocetStarychVozidiel + pocetNovychVozidiel);
		for (int i = 0; i < pocetStarychVozidiel + pocetNovychVozidiel; ++i)
		{
			int idVozidla = kStartId + i;
			double rychlost = i < pocetStarychVozidiel ? kRychlostStarychVozidiel : kRychlostNovychVozidiel;
			
			AgentVozidla vozidlo = new AgentVozidla(idVozidla, mySim, this, rychlost);
			_vozidla.add(vozidlo);
			addDynamicAgent(vozidlo);
		}
	}
	
	public boolean cakajuRolkyNaVozidlo()
	{
		return 0 < _rolkyCakajuceNaVozidlo_S4.size() || 0 < _rolkyCakajuceNaVozidlo_S3.size();
	}
	
	public void pridajRolkuDoFrontu(Sprava sprava)
	{
		if (sprava.rolka().cielovySklad() == Id.sklad3)
		{
			_rolkyCakajuceNaVozidlo_S3.add(sprava);
		}
		else
		{
			_rolkyCakajuceNaVozidlo_S4.add(sprava);
		}
	}
	
	public DotazVolneVozidlo dotazVolneVozidlo() { return _dotazVolneVozidlo; }
	public PravidloVstupnychRoliek pravidloVstupnychRoliek() { return _pravidloVstupnychRoliek; }
//	public SimQueue< Sprava > frontCakajucichNaVozidlo() { return _rolkyCakajuceNaVozidlo; }
	public int pocetVozidiel() { return _vozidla.size(); }
	public List< AgentVozidla > vozidla() { return _vozidla; }

	@Override
	public List< Sprava > rolky()
	{
		List< Sprava > rolky = new LinkedList<>();
		
		for (AgentVozidla vozidlo : _vozidla)
		{
			if (vozidlo.rolka() != null)
			{
				Sprava sprava = new Sprava(mySim());
				sprava.setRolka(vozidlo.rolka());
				rolky.add(sprava);
			}
		}
		return rolky;
	}
	
	public boolean jeVolneVozidlo()
	{
		for (AgentVozidla vozidlo : _vozidla)
		{
			if (vozidlo.jeVolne())
			{
				return true;
			}
		}
		return false;
	}
}
