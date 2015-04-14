package simulacia;

import entity.Objednavka;
import entity.Pokrm;
import entity.Pracovnik;
import entity.SkupinaZakaznikov;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Sprava extends MessageForm
{
	private Pracovnik _pracovnik;
	private SkupinaZakaznikov _zakaznici;
	private Objednavka _objednavka;
	private Pokrm _pokrm;
	
	public Sprava(Simulation mySim)
	{
		super(mySim);
		_pracovnik = null;
		_zakaznici = null;
		_objednavka = null;
		_pokrm = null;
	}

	private Sprava(Sprava original)
	{
		super(original);
		_pracovnik = original._pracovnik;
		_zakaznici = original._zakaznici;
		_objednavka = original._objednavka;
		_pokrm = original._pokrm;
	}
	
	@Override
	public Sprava createCopy()
	{
		return new Sprava(this);
	}

	public Pracovnik pracovnik()
	{ return _pracovnik; }

	public void setPracovnik(Pracovnik pracovnik)
	{ _pracovnik = pracovnik; }

	public SkupinaZakaznikov zakaznici()
	{ return _zakaznici; }

	public void setZakaznici(SkupinaZakaznikov zakaznici)
	{ _zakaznici = zakaznici; }
	
	public Objednavka objednavka()
	{ return _objednavka; }

	public void setObjednavka(Objednavka objednavka)
	{ _objednavka = objednavka; }
	
	public Pokrm pokrm()
	{ return _pokrm; }

	public void setPokrm(Pokrm pokrm)
	{ _pokrm = pokrm; }
}
