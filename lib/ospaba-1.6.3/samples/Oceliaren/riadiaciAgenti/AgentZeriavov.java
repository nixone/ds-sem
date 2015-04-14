package riadiaciAgenti;

import java.util.HashMap;
import java.util.Map;

import promptnyAsistenti.DotazVyberZeriavu;
import promptnyAsistenti.PravidloVystupnychRoliek;
import entity.Zeriav;
import kontinualnyAsistenti.ProcesPrelozeniaRolky;
import riadiaciManazeri.ManazerZeriavov;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import simulacia.Stanovisko;
import OSPABA.Agent;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPExceptions.SimException;

public class AgentZeriavov extends Agent
{
	// fronty
	private Map< Integer, SimQueue< Sprava > > _cakajuceSpravyDopravnik;
	private Map< Integer, SimQueue< Sprava > > _cakajuceSpravyVozidlo;

	private SimQueue< Sprava > _Z3_cakajuceS3;
	private SimQueue< Sprava > _Z3_cakajuceS4;
	private SimQueue< Sprava > _Z3_cakajuceNaVozidlo;
	
	// entity
	private Map< Integer, Zeriav > _zeriavy;

	// promptny asistenti
	private DotazVyberZeriavu _dotazVyberZeriavu;
	private PravidloVystupnychRoliek _pravidloVystupnychRoliek;

	// kontinualny asistenti
	private ProcesPrelozeniaRolky _procesPrelozeniaRolky;
//	private Process _procesPrelozeniaRolky;

	public AgentZeriavov(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		new ManazerZeriavov(Id.manazerZeriavov, mySim, this);

		_dotazVyberZeriavu = new DotazVyberZeriavu(Id.dotazVyberuZeriavu, mySim, this);
		_pravidloVystupnychRoliek = new PravidloVystupnychRoliek(Id.pravidloVystupnychRoliek, mySim, this);
		
		_procesPrelozeniaRolky = new ProcesPrelozeniaRolky(Id.procesPrelozeniaRolky, mySim, this);
		
		addOwnMessage(Mc.pridelenieZeriavu);
		addOwnMessage(Mc.prelozenieRolky);
		addOwnMessage(Mc.uvolnenieZeriavu);
		addOwnMessage(Mc.hold);
		
		_zeriavy = new HashMap<>(3);
		_cakajuceSpravyDopravnik = new HashMap<>(3);
		_cakajuceSpravyVozidlo = new HashMap<>(3);

		int [] idZeriavov = { Id.zeriav1, Id.zeriav2, Id.zeriav3 };
		for (int i = 0; i < idZeriavov.length; ++i)
		{
			_zeriavy.put(idZeriavov[i], new Zeriav(idZeriavov[i], mySim));
			_cakajuceSpravyDopravnik.put(idZeriavov[i], new SimQueue<>());
			_cakajuceSpravyVozidlo.put(idZeriavov[i], new SimQueue<>());
		}
		
		_Z3_cakajuceS3 = new SimQueue<>();
		_Z3_cakajuceS4 = new SimQueue<>();
		_Z3_cakajuceNaVozidlo = new SimQueue<>();
	}

	public ProcesPrelozeniaRolky procesPrelozeniaRolky()
	{ return _procesPrelozeniaRolky; }
	
	public DotazVyberZeriavu dotazVyberZeriavu()
	{ return _dotazVyberZeriavu; }
	
	public PravidloVystupnychRoliek pravidloVystupnychRoliek()
	{ return _pravidloVystupnychRoliek; }

	public Map<Integer, Zeriav> zeriavy()
	{ return _zeriavy; }

	public Zeriav zeriav(int idZeriavu)
	{ return _zeriavy.get(idZeriavu); }
	
	public Zeriav zeriav(Stanovisko stanovisko)
	{ 
		Zeriav zeriav = null;
		for (Zeriav z : _zeriavy.values())
		{
			if (z.stanovisko().equals(stanovisko))
			{
				zeriav = z;
				break;
			}
		}	
		return zeriav;
	}
	
	public void pridajRolkuDoFrontuCakajucichNaPridelenieZeriavu(Sprava sprava)
	{
		if (sprava.idZeriavu() == Id.zeriav3)
		{
			if (sprava.rolka().vozidlo() != null)
			{
				_Z3_cakajuceNaVozidlo.enqueue(sprava);
			}
			else if (sprava.idSkladu() == Id.sklad3) // sprava.rolka().sklad().id()
			{
				_Z3_cakajuceS3.enqueue(sprava);
			}
			else if (sprava.idSkladu() == Id.sklad4)
			{
				_Z3_cakajuceS4.enqueue(sprava);
			}
			else throw new SimException("Rolka je v stave ktory nemoze nastat");
		}
		else
		{
			_cakajuceSpravyDopravnik.get(sprava.idZeriavu()).enqueue(sprava);
		}
	}
	
	public Sprava prvaRolkaFrontuCakajucichNaPridelenieZeriavu(int idZeriavu)
	{
		if (idZeriavu == Id.zeriav3)
		{
			if (0 < _Z3_cakajuceNaVozidlo.size())
			{
				return _Z3_cakajuceNaVozidlo.get(0);
			}
			else if (0 < _Z3_cakajuceS3.size())
			{
				return _Z3_cakajuceS3.get(0);
			}
			else if (0 < _Z3_cakajuceS4.size())
			{
				return _Z3_cakajuceS4.get(0);
			}
			else return null;
		}
		else
		{
			return _cakajuceSpravyDopravnik.get(idZeriavu).peek();
		}
	}
	
	public int pocetCakajucichNaPridelenieZeriavu(int idZeriavu)
	{
		if (idZeriavu == Id.zeriav3)
		{
			return _Z3_cakajuceNaVozidlo.size() + _Z3_cakajuceS3.size() + _Z3_cakajuceS4.size(); // + _cakajuceSpravy.get(idZeriavu).size();
		}
		return _cakajuceSpravyDopravnik.get(idZeriavu).size();
	}

	public SimQueue< Sprava > cakajuceS3()
	{ return _Z3_cakajuceS3; }

	public SimQueue< Sprava > cakajuceS4()
	{ return _Z3_cakajuceS4; }
	
	public SimQueue< Sprava > cakajuceNaVozidlo()
	{ return _Z3_cakajuceNaVozidlo; }

	public SimQueue< Sprava > cakajuceNaZ12(int idZeriavu)
	{ return _cakajuceSpravyDopravnik.get(idZeriavu); }
}
