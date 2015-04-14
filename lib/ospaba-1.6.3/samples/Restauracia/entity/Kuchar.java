package entity;

import OSPABA.Simulation;

public class Kuchar extends Pracovnik
{
	private Objednavka _objednavka;
	
	public Kuchar(Simulation sim)
	{
		super(sim);
		_objednavka = null;
	}

	public void zacniPracu(Objednavka objednavka)
	{
		super.zacniPracu();
		_objednavka = objednavka;
	}
	
	@Override
	public void skonciPracu()
	{
		super.skonciPracu();
		_objednavka = null;
	}
	
	public Objednavka objednavka()
	{ return _objednavka; }

	public void setObjednavka(Objednavka objednavka)
	{ _objednavka = objednavka; }
	
	@Override
	public int cisloStola()
	{
		if (_objednavka != null)
		{
			return _objednavka.objednavajuci().stol().cislo();
		}
		return -1;
	}
}
