package entity;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Stol extends Entity
{
	private int _cislo;
	private int _pocetMiest;
	private boolean _jeObsadeny;
	private SkupinaZakaznikov _zakaznici;

	public Stol(Simulation mySim, int pocetMiest, int cisloStola)
	{
		super(mySim);
		_pocetMiest = pocetMiest;
		_jeObsadeny = false;
		_cislo = cisloStola;
		_zakaznici = null;
	}
	
	public int cislo()
	{ return _cislo; }
	
	public int pocetMiest()
	{ return _pocetMiest; }
	
	public boolean jeObsadeny()
	{ return _jeObsadeny; }
	
	public void setObsadeny(SkupinaZakaznikov zakaznici)
	{
		_zakaznici = zakaznici;
		_jeObsadeny = zakaznici != null;
	}
	
	public SkupinaZakaznikov zakaznici()
	{ return _zakaznici; }
}
