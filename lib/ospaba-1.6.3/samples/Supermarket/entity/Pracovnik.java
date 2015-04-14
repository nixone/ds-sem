package supermarket.entity;

import OSPABA.Entity;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;

public class Pracovnik extends Entity
{
//	private static int nextId = 0;

	private Zakaznik _obsluhovanyZakaznik;
	private SimQueue< Zakaznik > _front;
	private int _pocetObsluzenychZakaznikov;

	public Pracovnik(Simulation sim)
	{
		super(sim);
		
		_obsluhovanyZakaznik = null;
		_front = new SimQueue< Zakaznik >();
		_pocetObsluzenychZakaznikov = 0;
	}

	public Zakaznik obsluhovanyZakaznik()
	{ return _obsluhovanyZakaznik; }
	
	public void setObsluhovanyZakaznik(Zakaznik zakaznik)
	{ _obsluhovanyZakaznik = zakaznik; }
	
	public void pridajZakaznikaDoFrontu(Zakaznik zakaznik)
	{ _front.enqueue(zakaznik); }
	
	public Zakaznik vyberZakaznikaZFrontu()
	{ return _front.dequeue(); }
	
	public int dlzkaFrontu()
	{ return _front.size(); }
	
	public int pocetObsluzenychZakaznikov()
	{ return _pocetObsluzenychZakaznikov; }

	public void zvisPocetObsluzenychZakaznikov()
	{ ++_pocetObsluzenychZakaznikov; }

	public int pocetZakaznikov()
	{
		return dlzkaFrontu() + (_obsluhovanyZakaznik == null ? 0 : 1);
	}
	
	public synchronized Zakaznik zakaznik(int index)
	{
		if (index == 0)
		{
			return _obsluhovanyZakaznik;
		}
		if (0 < _front.size())
		{
			return _front.get(index-1);
		}
		return null;
	}
}
