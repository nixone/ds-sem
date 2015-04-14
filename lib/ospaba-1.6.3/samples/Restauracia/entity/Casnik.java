package entity;

import OSPABA.Simulation;

public class Casnik extends Pracovnik
{
	private SkupinaZakaznikov _obsluhovanyZakaznici;
	private String _stav;
	
	public Casnik(Simulation mySim)
	{
		super(mySim);
		_obsluhovanyZakaznici = null;
		_stav = "-";
	}
	
	public void zacniPracu(SkupinaZakaznikov obsluhovanyZakaznici)
	{
		super.zacniPracu();
		_obsluhovanyZakaznici = obsluhovanyZakaznici;
	}
	
	@Override
	public void skonciPracu()
	{
		super.skonciPracu();
		_obsluhovanyZakaznici = null;
		_stav = "-";
	}

	public SkupinaZakaznikov obsluhovanyZakaznici()
	{ return _obsluhovanyZakaznici; }

	public void setObsluhovanyZakaznici(SkupinaZakaznikov obsluhovanyZakaznici)
	{ _obsluhovanyZakaznici = obsluhovanyZakaznici; }

	public String stav()
	{ return _stav; }

	public void setStav(String stav)
	{ _stav = stav; }
	
	public int cisloStola()
	{
		if (_obsluhovanyZakaznici != null)
		{
			return _obsluhovanyZakaznici.stol().cislo();
		}
		return -1;
	}

}
