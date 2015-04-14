package entity;

import OSPABA.Entity;
import OSPABA.Simulation;

public class TimPracovnikov extends Entity
{
	private boolean _pracuje;
	private boolean _presuvaSa;
	private Sklad _sklad;
	private Rolka _rolka;

	private double _celkovyCasPrace;
	private double _casZaciatkuPrace;
	
	public TimPracovnikov(Simulation mySim, Sklad sklad)
	{
		super(mySim);
		
		_pracuje = false;
		_presuvaSa = false;
		_sklad = sklad;
		_rolka = null;
	}

	public boolean presuvaSa()
	{ return _presuvaSa; }
	
	public void setPresuvaSa(boolean presuvaSa)
	{ _presuvaSa = presuvaSa; }
	
	public boolean pracuje()
	{ return _pracuje; }
	
	public void setPracuje(boolean pracuje)
	{
		if (pracuje == true)
		{
			_casZaciatkuPrace = mySim().currentTime();
		}
		else
		{
			_celkovyCasPrace += mySim().currentTime() - _casZaciatkuPrace;
		}
		_pracuje = pracuje;
	}
	
	public double casPrace()
	{ return _celkovyCasPrace; }
	
	public Sklad sklad()
	{ return _sklad; }
	
	public void setSklad(Sklad sklad)
	{ _sklad = sklad; }
	
	public Rolka rolka()
	{ return _rolka; }
	
	public void setRolka(Rolka rolka)
	{ _rolka = rolka; }
}
