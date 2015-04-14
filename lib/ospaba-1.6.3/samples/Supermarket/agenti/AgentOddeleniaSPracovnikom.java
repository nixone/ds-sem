package supermarket.agenti;

import java.util.ArrayList;
import java.util.List;

import supermarket.asistenti.DotazNajkratsiFront;
import supermarket.asistenti.PlanovacOdchoduPracovnika;
import supermarket.entity.Pracovnik;
import supermarket.entity.Zakaznik;
import supermarket.simulacia.Id;
import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.Simulation;
import OSPStat.Stat;
import OSPStat.WStat;

public abstract class AgentOddeleniaSPracovnikom extends Agent
{
	private int _pocetPracovnikov;
	private List< Pracovnik > _pracovnici;
	private DotazNajkratsiFront _dotazNajkratsiFront;
	private PlanovacOdchoduPracovnika _planovacOdchoduPracovnika;
	
	private WStat _statistikaDlzkaFrontu;
	private Stat _statistikaCasCakania;
	
	protected ContinualAssistant _proces;
	
	private boolean _novySposoobPrace;
	
	public AgentOddeleniaSPracovnikom(int id, Simulation mySim, Agent parent, int pocetPracovnikov)
	{
		super(id, mySim, parent);

		_pocetPracovnikov = pocetPracovnikov;
		
		_pracovnici = new ArrayList< Pracovnik >(_pocetPracovnikov);
		for ( int i = 0; i < _pocetPracovnikov; ++i )
		{
			pridajPracovnika();
		}
		_dotazNajkratsiFront = new DotazNajkratsiFront(Id.najkratsiFrontZelenina, mySim, this, _pracovnici);
				
		_statistikaDlzkaFrontu = new WStat(mySim);
		_statistikaCasCakania = new Stat();
		
		_novySposoobPrace = false;
//		reset();
	}
	
	public void nastavNovySposobPrace()
	{
		_novySposoobPrace = true;	
		
		addOwnMessage(Mc.odchodPracovnika);
		
		_pracovnici.clear();
		pridajPracovnika();
		
		_planovacOdchoduPracovnika = new PlanovacOdchoduPracovnika(Id.odchodPracovnika, mySim(), this);
	}
	
//	@Override
//	public void reset()
//	{
//		super.reset();
//		_statistikaCasCakania.reset();
//		_statistikaDlzkaFrontu.reset();
//		
//		_pracovnici.clear();
//		for ( int i = 0; i < _pocetPracovnikov; ++i )
//		{
//			pridajPracovnika();
//		}
//	}
	
	public DotazNajkratsiFront dotazNajkratsiFront()
	{ return _dotazNajkratsiFront; }
	
	public Stat statistikaCasCakania()
	{ return _statistikaCasCakania; }
	
	public WStat statistikaDlzkaFrontu()
	{ return _statistikaDlzkaFrontu; }
	
	public boolean novySposobPrace()
	{ return _novySposoobPrace; }
	
	public ContinualAssistant proces()
	{ return _proces; }
	
	public void setProces(ContinualAssistant proces)
	{ _proces = proces; }
	
	public PlanovacOdchoduPracovnika planovacOdchoduPracovnika()
	{ return _planovacOdchoduPracovnika; }
	
	public List< Pracovnik > pracovnici()
	{ return _pracovnici; }
	
	public int maxPocetPracovnikov()
	{ return _pocetPracovnikov; }
	
	public Pracovnik pridajPracovnika()
	{
		Pracovnik p = new Pracovnik(mySim());
		_pracovnici.add(p);
		return p;
	}
	
	public int pocetZakaznikovVoFrontoch()
	{
		int pocet = 0;
		for (Pracovnik p : _pracovnici)
		{
			pocet += p.dlzkaFrontu();
		}
		return pocet;
	}
		
	public int pocetZakaznikov()
	{
		int pocet = pocetZakaznikovVoFrontoch();
		for (Pracovnik p : _pracovnici)
		{
			if (p.obsluhovanyZakaznik() != null)
			{
				++pocet;
			}
		}
		return pocet;
	}
	
	public Zakaznik zakaznik(int index)
	{
		int pocet = 0;
		for (Pracovnik p : _pracovnici)
		{
			if (index < pocet + p.pocetZakaznikov())
			{
				return p.zakaznik(index-pocet);
			}
			pocet += p.pocetZakaznikov();
		}
		return null;
	}
	
	public int indexPracovnika(int indexZakaznika)
	{
		int pocet = 0;
		int i = 0;
		for (Pracovnik p : _pracovnici)
		{
			if (indexZakaznika < pocet + p.pocetZakaznikov())
			{
				return i;
			}
			pocet += p.pocetZakaznikov();
			++i;
		}
		return -1;
	}
}
