package riadiaciAgenti;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import promptnyAsistenti.DotazVolnyTim;
import promptnyAsistenti.PravidloPrenunuTimov;
import kontinualnyAsistenti.ProcesOpracovaniaRolky;
import kontinualnyAsistenti.ProcesPredexpedicnehoSpracovania;
import kontinualnyAsistenti.ProcesPresunuTimuPracovnikov;
import riadiaciManazeri.ManazerSkladov;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import entity.Sklad;
import entity.TimPracovnikov;
import gui.RolkyDataSource;
import OSPABA.Agent;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;

public class AgentSkladov extends Agent implements RolkyDataSource
{
	private Map< Integer, Sklad > _sklady;
	private Map< Integer, SimQueue< Sprava > > _rolkyCakajuceNaOpracovanie;
	private Map< Integer, SimQueue< Sprava > > _rolkyCakajuceNaPridelenieMiesta;
	
	private List< TimPracovnikov > _timiPracovnikov;

	public AgentSkladov(int id, Simulation mySim, Agent parent, int pocetTimovPracovnikov)
	{
		super(id, mySim, parent);
		new ManazerSkladov(Id.manazerSkladov, mySim, this);

		new ProcesOpracovaniaRolky(Id.procesOpracovaniaRolky, mySim, this);
		new ProcesPredexpedicnehoSpracovania(Id.procesPredexpedicnehoSpracovaniaRolky, mySim, this);
		
		new DotazVolnyTim(Id.dotazVolnyTim, mySim, this);
		new PravidloPrenunuTimov(Id.pravidloPresunuTimov, mySim, this);
		
		new ProcesPresunuTimuPracovnikov(Id.procesPresunuTimuPracovnikov, mySim, this);
		
		addOwnMessage(Mc.initRolkaSkladu);
		addOwnMessage(Mc.pridelenieMiestaVSklade);
		addOwnMessage(Mc.opracovanieRolky);
		addOwnMessage(Mc.odstranenieRolkyZoSkladu);
		addOwnMessage(Mc.hold);

		_sklady = new HashMap<>(4);
		_rolkyCakajuceNaOpracovanie = new HashMap<>(3);
		_rolkyCakajuceNaPridelenieMiesta = new HashMap<>(4);
		
		int [] idSkladov = { Id.sklad1, Id.sklad2, Id.sklad3, Id.sklad4 };
		int [] kapacity = { 120, 110, 70, 170 };
		for (int i = 0; i < idSkladov.length; ++i)
		{
			_sklady.put(idSkladov[i], new Sklad(idSkladov[i], mySim, kapacity[i]));
			_rolkyCakajuceNaPridelenieMiesta.put(idSkladov[i], new SimQueue<>());
			
			if (idSkladov[i] != Id.sklad4)
			{
				_rolkyCakajuceNaOpracovanie.put(idSkladov[i], new SimQueue<>());
			}
		}
		_timiPracovnikov = new LinkedList<>();
				
		for (int i = 0; i < pocetTimovPracovnikov; ++i)
		{
			_timiPracovnikov.add(new TimPracovnikov(mySim, _sklady.get(idSkladov[i % 3])));
		}
	}

	public Sklad sklad(int idSkladu)
	{ return _sklady.get(idSkladu); }
	
	public SimQueue< Sprava > rolkyCakajuceNaOpracovanie(int idSkladu)
	{
		if (idSkladu == Id.sklad4)
		{
			return null;
		}
		return _rolkyCakajuceNaOpracovanie.get(idSkladu);
	}
	
	public SimQueue< Sprava > rolkyCakajuceNaPridelenieMiesta(int idSkladu)
	{
		return _rolkyCakajuceNaPridelenieMiesta.get(idSkladu);
	}
	
	public List< TimPracovnikov > timiPracovnikov()
	{ return _timiPracovnikov; }
	

	public synchronized Map< Integer, Sklad > sklady()
	{ return _sklady; }

	@Override
	public List< Sprava > rolky()
	{
		List< Sprava > rolky = new LinkedList<>();
		
		for (Sklad sklad : _sklady.values())
		{
			try {
				rolky.addAll(sklad.rolky());
			}
			catch (RuntimeException ex) {}
		}
		return rolky;
	}
}
