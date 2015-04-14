package agenti;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import asistenti.AkciaPriradStol;
import asistenti.DotazNajkratsiePracujuciPracovnik;
import asistenti.ProcesJedenia;
import asistenti.ProcesObjednavania;
import asistenti.ProcesPlatenia;
import asistenti.ProcesPrineseniaJedla;
import entity.Casnik;
import entity.Pracovnik;
import entity.SkupinaZakaznikov;
import entity.Stol;
import manazeri.ManazerJedalne;
import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;

public class AgentJedalne extends Agent
{
	// zdroje obsluhy
	private List< Stol > _stoli;
	private List< Pracovnik > _casnici;
	
	// fronty zakaznikov
	private SimQueue< SkupinaZakaznikov > _cakajuciNaObjednanieJedla;
	private SimQueue< SkupinaZakaznikov > _cakajuciNaPrinesenieJedla;
	private SimQueue< SkupinaZakaznikov > _cakajuciNaPlatenie;
	
	// promptny asistenti
	private AkciaPriradStol _akciaPriradStol;
	private DotazNajkratsiePracujuciPracovnik _dotazNajkratsiePracujuciCasnik;
	
	// kontinualny asistenti
	private ProcesObjednavania _procesObjednavania;
	private ProcesPrineseniaJedla _procesPrineseniaJedla;
	private ProcesJedenia _procesJedenia;
	private ProcesPlatenia _procesPlatenia;
	
	// statistiky
	private WStat _pocetVolnychCasnikovStat;

	public AgentJedalne(int id, Simulation mySim, Agent parent, int pocetCasnikov)
	{
		super(id, mySim, parent);

		new ManazerJedalne(Id.manazerJedalne, mySim, this);
		
		addOwnMessage(Mc.obsluhaZakaznika);
		addOwnMessage(Mc.pripravaJedlaUkoncena);

		addOwnMessage(Mc.objednanieUkoncene);
		addOwnMessage(Mc.prinesenieUkoncene);
		addOwnMessage(Mc.jedenieUkoncene);
		addOwnMessage(Mc.platenieUkoncene);
		
		_stoli = new ArrayList< Stol >();
		int [] poctyMiest = {2, 4, 6};
		int [] poctyStolov = {10, 7, 5};

		int cisloStola = 1;
		for (int i = 0; i < poctyStolov.length; ++i)
		{
			for (int j = 0; j < poctyStolov[i]; ++j)
			{
				_stoli.add(new Stol(mySim, poctyMiest[i], cisloStola));
				++cisloStola;
			}
		}
		
		_casnici = new ArrayList< Pracovnik >(pocetCasnikov);
		for (int i = 0; i < pocetCasnikov; ++i)
		{
			_casnici.add(new Casnik(mySim));
		}
		
		_cakajuciNaObjednanieJedla = new SimQueue< SkupinaZakaznikov >(new WStat(mySim));
		_cakajuciNaPrinesenieJedla = new SimQueue< SkupinaZakaznikov >(new WStat(mySim));
		_cakajuciNaPlatenie = new SimQueue< SkupinaZakaznikov >(new WStat(mySim));
		
		_akciaPriradStol = new AkciaPriradStol(Id.akciaPriradStol, mySim, this, _stoli);
		_dotazNajkratsiePracujuciCasnik =
				new DotazNajkratsiePracujuciPracovnik(Id.dotazNajkratsiePracujuciCasnik, mySim, this, _casnici);
		
		_procesObjednavania = new ProcesObjednavania(Id.procesObjednavania, mySim, this);
		_procesPrineseniaJedla = new ProcesPrineseniaJedla(Id.procesPrineseniaJedla, mySim, this);
		_procesJedenia = new ProcesJedenia(Id.procesJedenia, mySim, this);
		_procesPlatenia = new ProcesPlatenia(Id.procesPlatenia, mySim, this);
		
		_pocetVolnychCasnikovStat = new WStat(mySim);
	}
	
	public AkciaPriradStol akciaPriradStol()
	{ return _akciaPriradStol; }
	
	public DotazNajkratsiePracujuciPracovnik dotazNajkratsiePracujuciCasnik()
	{ return _dotazNajkratsiePracujuciCasnik; }

	public SimQueue<SkupinaZakaznikov> cakajuciNaObjednanieJedla()
	{ return _cakajuciNaObjednanieJedla; }

	public SimQueue<SkupinaZakaznikov> cakajuciNaPrinesenieJedla()
	{ return _cakajuciNaPrinesenieJedla; }

	public SimQueue<SkupinaZakaznikov> cakajuciNaPlatenie()
	{ return _cakajuciNaPlatenie; }

	public ProcesObjednavania procesObjednavania()
	{ return _procesObjednavania; }

	public ProcesPrineseniaJedla procesPrineseniaJedla()
	{ return _procesPrineseniaJedla; }

	public ProcesJedenia procesJedenia()
	{ return _procesJedenia; }

	public ProcesPlatenia procesPlatenia()
	{ return _procesPlatenia; }
	
	public List< Pracovnik > casnici()
	{ return _casnici; }
	
	public List< Stol > stoli()
	{ return _stoli; }
	
	public int pocetVolnychCasnikov()
	{
		int pocet = 0;
		for (Pracovnik casnik : _casnici)
		{
			if (! casnik.pracuje())
			{
				++pocet;
			}
		}
		return pocet;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized List< SkupinaZakaznikov > zakaznici()
	{
		List< SkupinaZakaznikov > zakaznici = new LinkedList< SkupinaZakaznikov >();

		try {
		SimQueue [] frontyZakaznikov = {
				_cakajuciNaObjednanieJedla,
				_cakajuciNaPrinesenieJedla,
				_cakajuciNaPlatenie
			};
		for (SimQueue< SkupinaZakaznikov > front : frontyZakaznikov)
		{
			for (SkupinaZakaznikov z : front)
			{
				zakaznici.add(z);
			}
		}
		for (SkupinaZakaznikov z : _procesJedenia.zakaznici())
		{
			zakaznici.add(z);
		}
		for (Pracovnik casnik : _casnici)
		{
			if (casnik.pracuje())
			{
				zakaznici.add(((Casnik)casnik).obsluhovanyZakaznici());
			}
		}
		}
		catch (RuntimeException e)
		{
			// .
		}
		return zakaznici;
	}

	public WStat pocetVolnychCasnikovStat()
	{ return _pocetVolnychCasnikovStat; }
	
//	public int pocetZakaznikov()
//	{
//		return pocetZakaznikovVoFronte(_cakajuciNaObjednanieJedla)
//			 + pocetZakaznikovVoFronte(_cakajuciNaPrinesenieJedla)
//			 + pocetZakaznikovVoFronte(_cakajuciNaPlatenie)
//			 + pocetPraveObsluhovanychZakaznikov();
//	}
//	
//	private int pocetZakaznikovVoFronte(SimQueue< SkupinaZakaznikov > front)
//	{
//		int pocet = 0;
//		for (SkupinaZakaznikov zakaznici : front)
//		{
//			pocet += zakaznici.pocet();
//		}
//		return pocet;
//	}
//	
//	private int pocetPraveObsluhovanychZakaznikov()
//	{
//		int pocet = 0;
//		for (Pracovnik casnik : _casnici)
//		{
//			if (casnik.pracuje())
//			{
//				pocet += ((Casnik)casnik).obsluhovanyZakaznici().pocet();
//			}
//		}
//		return pocet;
//	}
}
