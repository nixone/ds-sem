package entity;

import exceptions.DopravnikPrazdnyException;
import exceptions.PrekrocenaKapacitaException;
import simulacia.Sprava;
import OSPABA.Entity;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPExceptions.SimException;
import OSPStat.Stat;
import OSPStat.WStat;

public class KolekciaRoliek extends Entity
{
	private SimQueue< Sprava > _rolky;
	private int _kapacita;

	public KolekciaRoliek(int id, Simulation mySim, int kapacita)
	{
		super(id, mySim);
		
		_rolky = new SimQueue<>(new WStat(mySim));
		_kapacita = kapacita;
	}
	
	public SimQueue< Sprava > rolky()
	{
		return _rolky;
	}
	
	public void pridajRolku(Sprava sprava) throws PrekrocenaKapacitaException
	{
		if (pocetRoliek() < _kapacita)
		{
			_rolky.enqueue(sprava);
		}
		else throw new PrekrocenaKapacitaException("Prekročená kapacita dopravnika " + this);
	}
	
	public void odstranRolku(Sprava sprava)
	{
		boolean removed = _rolky.remove(sprava);
		
		if (! removed) throw new SimException("Pokus o odstranenie neexistujucej rolky " + this);
	}
	
	public Sprava odstranRolku() throws DopravnikPrazdnyException
	{
		if (0 < pocetRoliek())
		{
			return _rolky.dequeue();
		}
		else throw new SimException("Pokus o odstranenie neexistujucej rolky " + this);
	}

	public double zaplnenie()
	{
		return pocetRoliek() / (double)kapacita();
	}

	public int pocetRoliek()
	{ return _rolky.size(); }
	
	public int kapacita()
	{ return _kapacita; }
	
	public boolean jePrazdny()
	{ return pocetRoliek() == 0; }
	
	public Stat statistika()
	{ return _rolky.lengthStatistic(); }
}
