package entity;

import dynamickyAgenti.AgentVozidla;
import simulacia.Id;
import simulacia.Stanovisko;
import OSPABA.Entity;
import OSPABA.Simulation;
import OSPDataStruct.SimStack;
import OSPExceptions.SimException;

public class Zeriav extends Entity
{
	private boolean _pracuje;
	private String _ciel;
	private Rolka _rolka;
	private SimStack< AgentVozidla > _pristaveneVozidla;
	
	private double _celkovyCasPrace;
	private double _casZaciatkuPrace;

	public Zeriav(int id, Simulation mySim)
	{
		super(id, mySim);
		_pracuje = false;
		_rolka = null;
		_ciel = null;
		_pristaveneVozidla = new SimStack<>();
	}

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
	
	public Rolka rolka()
	{ return _rolka; }
	
	public void setRolka(Rolka rolka)
	{ _rolka = rolka; }
	
	public AgentVozidla peekPristaveneVozidlo()
	{
		if (_pristaveneVozidla.size() == 0) return null;
		return _pristaveneVozidla.peek();
	}
	
	public AgentVozidla popPristaveneVozidlo()
	{ return _pristaveneVozidla.pop(); }
	
	public void pushPristaveneVozidlo(AgentVozidla pristaveneVozidlo)
	{ _pristaveneVozidla.push(pristaveneVozidlo); }
	
	public Stanovisko stanovisko()
	{
		switch (id())
		{
		case Id.zeriav1:
			return Stanovisko.S1;
		
		case Id.zeriav2:
			return Stanovisko.S2;
		
		case Id.zeriav3:
			return Stanovisko.S3;
		}
		throw new SimException("neexistujuci zeriav");
	}
	
	public String cislo()
	{
		switch (id())
		{
		case Id.zeriav1: return "Z1";
		case Id.zeriav2: return "Z2";
		case Id.zeriav3: return "Z3";
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return cislo();
	}
	
	public String ciel()
	{ return _ciel; }
	
	public void setCiel(String ciel)
	{ _ciel = ciel;}
}
