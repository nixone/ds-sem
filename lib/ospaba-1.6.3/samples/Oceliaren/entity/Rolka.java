package entity;

import simulacia.Stanovisko;
import dynamickyAgenti.AgentVozidla;
import OSPABA.Entity;
import OSPABA.Simulation;

public class Rolka extends Entity
{
	private int _cielovySklad;
	private boolean _jeOpracovana;
	private boolean _jePripravenaNaExpedovanie;
	
	private Sklad _sklad;
	private AgentVozidla _vozidlo;
	private Dopravnik _dopravnik;
	
	private Stanovisko _stanovisko;

	public Rolka(Simulation mySim, int cielovySklad)
	{
		super(mySim);
		
		_cielovySklad = cielovySklad;
		_jeOpracovana = false;
		_jePripravenaNaExpedovanie = false;
		
		_sklad = null;
		_vozidlo = null;
		_dopravnik = null;
		_stanovisko = null;
	}
	
	public Rolka(Simulation mySim, boolean jeOpracovana, boolean jePripravenaNaExpedovanie)
	{
		super(mySim);
		
		_cielovySklad = -1;
		_jeOpracovana = jeOpracovana;
		_jePripravenaNaExpedovanie = jePripravenaNaExpedovanie;
		
		_sklad = null;
		_dopravnik = null;
		_vozidlo = null;
	}
	
	public Stanovisko stanovisko()
	{
		return _stanovisko;
		// rolka moze byt v sklade xor vo vozidle xor na dopravniku
//		assert(1 == (_sklad == null ? 0:1) + (_vozidlo == null ? 0:1) + (_dopravnik == null ? 0:1));
//				
//		if (_sklad != null)
//		{
//			return _sklad.stanovisko();
//		}
//		else if (_dopravnik != null)
//		{
//			return _dopravnik.stanovisko();
//		}
//		else if (_vozidlo != null)
//		{
//			return _vozidlo.stanovisko();
//		}
//		else throw new SimException("Rolka nie je nikde priradena");
	}
	
	public void setStanovisko(Stanovisko stanovisko)
	{ _stanovisko = stanovisko; }
	
	public int cielovySklad()
	{ return _cielovySklad; }
	
	public void setCielovySklad(int cielovySklad)
	{ _cielovySklad = cielovySklad; }
	
	public Sklad sklad()
	{ return _sklad; }
	
	public void setSklad(Sklad sklad)
	{
		_sklad = sklad;
		
		if (_sklad != null) _stanovisko = _sklad.stanovisko();
	}

	public boolean jeOpracovana()
	{ return _jeOpracovana; }
	
	public void setOpracovana(boolean jeOpracovana)
	{ _jeOpracovana = jeOpracovana; }
	
	public boolean jePripravenaNaExpedovanie()
	{ return _jePripravenaNaExpedovanie; }
	
	public void setPripravenaNaExpedovanie(boolean jePripravenaNaExpedovanie)
	{ _jePripravenaNaExpedovanie = jePripravenaNaExpedovanie; }
	
	public AgentVozidla vozidlo()
	{ return _vozidlo; }
	
	public void setVozidlo(AgentVozidla vozidlo)
	{
		if (_vozidlo != null) _vozidlo.setRolka(null);
		if (vozidlo != null) vozidlo.setRolka(this);

		_vozidlo = vozidlo;	
		
		if (_vozidlo != null) _stanovisko = _vozidlo.stanovisko();
	}
	
	public Dopravnik dopravnik()
	{ return _dopravnik; }
	
	public void setDopravnik(Dopravnik dopravnik)
	{
		_dopravnik = dopravnik;
		
		if (_dopravnik != null) _stanovisko = _dopravnik.stanovisko();
	}
}
