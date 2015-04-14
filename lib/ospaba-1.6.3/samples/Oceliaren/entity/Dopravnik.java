package entity;

import simulacia.Id;
import simulacia.Sprava;
import simulacia.Stanovisko;
import exceptions.DopravnikPrazdnyException;
import OSPABA.Simulation;
import OSPExceptions.SimException;

public class Dopravnik extends KolekciaRoliek
{
	private boolean _jeVstupnyDopravnik;

	public Dopravnik(int id, Simulation mySim, int kapacitaOdkladacichMiest, boolean jeVstupnyDopravnik)
	{
		super(id, mySim, kapacitaOdkladacichMiest);
		_jeVstupnyDopravnik = jeVstupnyDopravnik;
	}
	
	@Override
	public void odstranRolku(Sprava sprava) throws DopravnikPrazdnyException
	{
		try
		{
			super.odstranRolku(sprava);
		}
		catch (SimException ex)
		{
			throw new DopravnikPrazdnyException("Pokus o odobratie rolky s prázdneho dopravníka " + cislo());
		}
	}
	
	@Override
	public Sprava odstranRolku() throws DopravnikPrazdnyException
	{
		try
		{
			return super.odstranRolku();
		}
		catch (SimException ex)
		{
			throw new DopravnikPrazdnyException("Pokus o odobratie rolky s prázdneho dopravníka " + cislo());
		}
	}

	public boolean jeVstupnyDopravnik()
	{ return _jeVstupnyDopravnik; }
	
	public boolean jeVystupnyDopravnik()
	{ return ! _jeVstupnyDopravnik; }
	
	public Stanovisko stanovisko()
	{
		switch (id())
		{
		case Id.dopravnik1: return Stanovisko.S1;
		case Id.dopravnik2: return Stanovisko.S2;
		case Id.dopravnik3: return Stanovisko.S3;
		}
		throw new SimException("neexistujuci dopravnik");
	}
	
	public String cislo()
	{
		switch (id())
		{
		case Id.dopravnik1: return "D1";
		case Id.dopravnik2: return "D2";
		case Id.dopravnik3: return "D3";
		}
		throw new SimException("neexistujuci dopravnik");
	}
	
	@Override
	public String toString()
	{
		return cislo();
	}
	
	public boolean jeZaplneny()
	{
		return pocetRoliek() == kapacita();
	}
}
