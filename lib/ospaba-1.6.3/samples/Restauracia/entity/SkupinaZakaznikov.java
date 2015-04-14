package entity;

import java.util.ArrayList;
import java.util.List;

import OSPABA.Entity;
import OSPABA.Simulation;
import OSPExceptions.SimException;

public class SkupinaZakaznikov extends Entity
{
	private Stol _stol;
	private String _stav;

	private List< Zakaznik > _zakaznici;
	private boolean _odchadzajuNeobsluzeny;
	
	private Objednavka _objednavka;

	public SkupinaZakaznikov(Simulation mySim, int pocetZakaznikov)
	{
		super(mySim);

		_stol = null;
		_stav = "";

		_odchadzajuNeobsluzeny = false;
		_zakaznici = new ArrayList<>();
		
		for (int i = 0; i < pocetZakaznikov; ++i)
		{
			_zakaznici.add(new Zakaznik(mySim));
		}
		
		_objednavka = new Objednavka(mySim(), this);
		for (Zakaznik zakaznik : _zakaznici)
		{
			_objednavka.pridajPokrm(zakaznik.vyberPokrm());
		}
	}
	
	public Objednavka objednavka()
	{
		return _objednavka;
	}

	public Stol stol()
	{ return _stol; }

	public void setStol(Stol stol)
	{
		if (stol == null)
		{
			if (_stol != null)
			{
				uvolniStol();
			}
		}
		else
		{
			if (stol.jeObsadeny())
			{
				throw new SimException("pokus o priradenie uz priradeneho stolu");
			}
			stol.setObsadeny(this);
		}
		_stol = stol;
	}
	
	public void uvolniStol()
	{
		_stol.setObsadeny(null);
	}

	public List<Zakaznik> zakaznici()
	{ return _zakaznici; }
	
	public int pocet()
	{
		return _zakaznici.size();
	}

	public boolean odchadzajuNeobsluzeny()
	{ return _odchadzajuNeobsluzeny; }

	public void setOdchadzajuNeobsluzeny(boolean odchadzajuNeobsluzeny)
	{ _odchadzajuNeobsluzeny = odchadzajuNeobsluzeny; }
	
	public void zaciatokCakania()
	{
		for (Zakaznik zakaznik : _zakaznici)
		{
			zakaznik.zaciatokCakania();
		}
	}
	
	public void koniecCakania()
	{
		for (Zakaznik zakaznik : _zakaznici)
		{
			zakaznik.koniecCakania();
		}
	}
	
	public double casCakania()
	{
		return _zakaznici.get(0).casCakania();
	}
	
	public double casZaciatkuCakania()
	{
		return _zakaznici.get(0).casZaciatkuCakania();
	}
	
	public boolean cakaju()
	{
		return _zakaznici.get(0).caka();
	}
	
	public String stav()
	{ return _stav; }

	public void setStav(String stav)
	{ _stav = stav; }
}
