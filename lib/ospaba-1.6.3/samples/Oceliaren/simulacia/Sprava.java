package simulacia;

import dynamickyAgenti.AgentVozidla;
import entity.Rolka;
import entity.TimPracovnikov;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Sprava extends MessageForm
{
	private Rolka _rolka;
	private AgentVozidla _vozidlo;
	private Stanovisko _ciel;
	private TimPracovnikov _tim;
	
	private int _dopravnik;
	private int _zeriav;
	private int _sklad;
	
	private String _log;

	public Sprava(Simulation mySim)
	{
		super(mySim);

		_rolka = null;
		_vozidlo = null;
		_ciel = null;
		_tim = null;
		_dopravnik = -1;
		_zeriav = -1;
		_sklad = -1;
		_log = null;
	}

	public Sprava(Sprava original)
	{
		super(original);
		copy(original);
	}
	
	@Override
	public MessageForm createCopy()
	{
		return new Sprava(this);
	}
	
	public void copy(Sprava original)
	{
		super.copy(original);	
		_rolka = original._rolka;
		_vozidlo = original._vozidlo;
		_ciel = original._ciel;
		_tim = original._tim;
		_dopravnik = original._dopravnik;
		_zeriav = original._zeriav;
		_sklad = original._sklad;
		_log = original._log;
	}
	
	public Rolka rolka()
	{ return _rolka; }
	
	public void setRolka(Rolka rolka)
	{ _rolka = rolka; }

	public AgentVozidla vozidlo()
	{ return _vozidlo; }

	public void setVozidlo(AgentVozidla vozidlo)
	{ _vozidlo = vozidlo; }

	public Stanovisko ciel()
	{ return _ciel; }

	public void setCiel(Stanovisko ciel)
	{ _ciel = ciel; }

	public int idDopravnika()
	{ return _dopravnik; }

	public void setIdDopravnika(int dopravnik)
	{ _dopravnik = dopravnik; }

	public int idZeriavu()
	{ return _zeriav; }

	public void setIdZeriavu(int zeriav)
	{ _zeriav = zeriav; }

	public int idSkladu()
	{ return _sklad; }

	public void setIdSkladu(int sklad)
	{ _sklad = sklad; }
	
	public TimPracovnikov timPracovnikov()
	{ return _tim; }
	
	public void setTimPracovnikov(TimPracovnikov tim)
	{ _tim = tim; }
	
	public String log()
	{ return _log; }
	
	public void setLog(String log)
	{ _log = log; }
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Sprava)
		{
			if (_rolka.id() == ((Sprava)obj)._rolka.id())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + " " + rolka().id();
	}
}
