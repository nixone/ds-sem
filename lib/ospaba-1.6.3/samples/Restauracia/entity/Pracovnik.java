package entity;

import OSPABA.Entity;
import OSPABA.Simulation;

public abstract class Pracovnik extends Entity
{
	private boolean _pracuje;
	private double _celkovyCasPrace;
	private double _casZaciatkuPrace;

	public Pracovnik(Simulation mySim)
	{
		super(mySim);
		_pracuje = false;
		_celkovyCasPrace = .0;
		_casZaciatkuPrace = .0;
	}
	
	public void zacniPracu()
	{
		_casZaciatkuPrace = mySim().currentTime();
		_pracuje = true;
	}
	
	public void skonciPracu()
	{
		_celkovyCasPrace += mySim().currentTime() - _casZaciatkuPrace;
		_pracuje = false;
	}

	public boolean pracuje()
	{ return _pracuje; }

	public double casPrace()
	{ return _celkovyCasPrace; }
	
	public double casZaciatkuPrace()
	{ return _casZaciatkuPrace; }
	
	public abstract int cisloStola();
}
