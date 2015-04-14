package supermarket.simulacia;

import supermarket.entity.Pracovnik;
import supermarket.entity.Zakaznik;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Sprava extends MessageForm
{
	private Zakaznik _zakaznik;
	private Pracovnik _pracovnik;
	private int _pocetObsluzenychZakaznikov;

	/**
	 * Kopirovaci konstruktor
	 */
	public Sprava(Sprava original)
	{
		super(original);
		_zakaznik = original._zakaznik;
		_pracovnik = original._pracovnik;
		_pocetObsluzenychZakaznikov = original._pocetObsluzenychZakaznikov;
	}

	public Sprava(Simulation mySim, Zakaznik zakaznik)
	{
		super(mySim);
		_zakaznik = zakaznik;
		_pracovnik = null;
		_pocetObsluzenychZakaznikov = 0;
	}

	public Zakaznik zakaznik()
	{ return _zakaznik; }

	public void setZakaznik(Zakaznik zakaznik)
	{ _zakaznik = zakaznik; }
	
	public Pracovnik pracovnik()
	{ return _pracovnik; }

	public void setPracovnik(Pracovnik pracovnik)
	{ _pracovnik = pracovnik; }
	
	public int pocetObsluzenychZakaznikov()
	{ return _pocetObsluzenychZakaznikov; }

	public void setPocetObsluzenychZakaznikov(int pocet)
	{ _pocetObsluzenychZakaznikov = pocet; }
}
