package dynamickyAgenti;

import kontinualnyAsistenti.ProcesPresunuVozidla;
import riadiaciAgenti.AgentVozidiel;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Stanovisko;
import dynamickyManazeri.ManazerVozidla;
import entity.Rolka;
import OSPABA.Agent;
import OSPABA.DynamicAgent;
import OSPABA.Simulation;

public class AgentVozidla extends DynamicAgent
{
	private boolean _jeVolne;
	private boolean _stoji;
	private final double _rychlost; // [m/min]
	
	private Stanovisko _stanovisko;
	private Stanovisko _ciel;
	private Rolka _rolka;
	
	private ProcesPresunuVozidla _procesPresunu;
	
	private double _celkovyCasPrace;
	private double _casZaciatkuPrace;
	
	public AgentVozidla(int id, Simulation mySim, Agent parent, double rychlost)
	{
		super(id, mySim, parent);
		new ManazerVozidla(id * 100, mySim, this);
		_procesPresunu = new ProcesPresunuVozidla(Id.procesPresunuVozidla, mySim, this);

		addOwnMessage(Mc.hold);
		
		_jeVolne = true;
		_stoji = true;
		_rychlost = rychlost;
		_stanovisko = Stanovisko.S1;
		_rolka = null;
	}
	
	public double casPrace()
	{ return _celkovyCasPrace; }
	
	public double casZaciatkuPrace()
	{ return _casZaciatkuPrace; }
	
	public void zvisCelkovyCasPrace(double cas)
	{ _celkovyCasPrace += cas; }
	
	public void setCasZaciatkuPrace(double cas)
	{ _casZaciatkuPrace = cas; }

	public double rychlost()
	{ return _rychlost; }
	
	public Stanovisko stanovisko()
	{ return _stanovisko; }
	
	public void setStanovisko(Stanovisko stanovisko)
	{ _stanovisko = stanovisko; }
	
	public Stanovisko ciel()
	{ return _ciel; }
	
	public void setCiel(Stanovisko ciel)
	{ _ciel = ciel; }
	
	public boolean jeVolne()
	{ return _jeVolne; }
	
	public void setVolne(boolean jeVolne)
	{ _jeVolne = jeVolne; }
	
	public boolean stoji()
	{ return _stoji; }
	
	public void setStoji(boolean stoji)
	{ _stoji = stoji; }
	
	public ProcesPresunuVozidla procesPresunu()
	{ return _procesPresunu; }
	
	public Rolka rolka()
	{ return _rolka; }
	
	public void setRolka(Rolka rolka)
	{ _rolka = rolka; }
	
	public String cislo()
	{
		return "V" + (id() - AgentVozidiel.kStartId + 1);
	}
	
	@Override
	public String toString()
	{
		return cislo();
	}
}
