package agenti;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import asistenti.DotazNajkratsiePracujuciPracovnik;
import asistenti.ProcesPripravyJedla;
import entity.Kuchar;
import entity.Objednavka;
import entity.Pracovnik;
import entity.SkupinaZakaznikov;
import manazeri.ManazerKuchyne;
import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;

public class AgentKuchyne extends Agent
{
	// entity
	private List< Pracovnik > _kuchari;
	private SimQueue< Objednavka > _objednavky;
	
	// asistenti
	private DotazNajkratsiePracujuciPracovnik _dotazNajkratsiePracujuciKuchar;
	private ProcesPripravyJedla _procesPripravyJedla;
	
	// statistiky
	private WStat _pocetVolnychKucharovStat;
	
	public AgentKuchyne(int id, Simulation mySim, Agent parent, int pocetKucharov)
	{
		super(id, mySim, parent);

		new ManazerKuchyne(Id.manazerKuchyne, mySim, this);
	
		addOwnMessage(Mc.pripravaJedla);
		addOwnMessage(Mc.pripravaJedlaUkoncena);
		
		_objednavky = new SimQueue< Objednavka >();
		_kuchari = new ArrayList< Pracovnik >(pocetKucharov);
		for (int i = 0; i < pocetKucharov; ++i)
		{
			_kuchari.add(new Kuchar(mySim));
		}
		
		_procesPripravyJedla = new ProcesPripravyJedla(Id.procesPripravyJedla, mySim, this);
		_dotazNajkratsiePracujuciKuchar =
				new DotazNajkratsiePracujuciPracovnik(Id.dotazNajkratsiePracujuciKuchar, mySim, this, _kuchari);
		
		_pocetVolnychKucharovStat = new WStat(mySim);
	}

	public List< Pracovnik > kuchari()
	{ return _kuchari; }

	public SimQueue< Objednavka > frontObjednavok()
	{ return _objednavky; }
	
	public ProcesPripravyJedla procesPripravyJedla()
	{ return _procesPripravyJedla; }
	
	public DotazNajkratsiePracujuciPracovnik dotazNajkratsiePracujuciKuchar()
	{ return _dotazNajkratsiePracujuciKuchar; }
	
	public synchronized List< Objednavka > objednavky()
	{
		List< Objednavka > objednavky = new LinkedList< Objednavka >();
		Set< Objednavka > spracovavane = new HashSet< Objednavka >();

		for (Pracovnik kuchar : _kuchari)
		{
			if (kuchar.pracuje())
			{
				spracovavane.add(((Kuchar)kuchar).objednavka());
			}
		}
		spracovavane.addAll(frontObjednavok());
		objednavky.addAll(spracovavane);
		
		return objednavky;
	}
	
	public synchronized List< SkupinaZakaznikov > zakaznici()
	{
		List< SkupinaZakaznikov > zakaznici = new LinkedList< SkupinaZakaznikov >();
		Set< SkupinaZakaznikov > spracovavany = new HashSet< SkupinaZakaznikov >();

		for (Objednavka objednavka : _objednavky)
		{
			spracovavany.add(objednavka.objednavajuci());
		}
		for (Pracovnik kuchar : _kuchari)
		{
			if (kuchar.pracuje())
			{
				spracovavany.add(((Kuchar)kuchar).objednavka().objednavajuci());
			}
		}
		zakaznici.addAll(spracovavany);

		return zakaznici;
	}
	
	public int pocetVolnychKucharov()
	{
		int pocet = 0;
		for (Pracovnik kuchar : _kuchari)
		{
			if (! kuchar.pracuje())
			{
				++pocet;
			}
		}
		return pocet;
	}
	
	public WStat pocetVolnychKucharovStat()
	{ return _pocetVolnychKucharovStat; }
}
