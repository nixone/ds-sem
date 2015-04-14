package agenti;

import java.util.LinkedList;
import java.util.List;

import asistenti.PlanovacOtvoreniaRestauracie;
import asistenti.PlanovacPrichoduZakaznikov;
import entity.SkupinaZakaznikov;
import manazeri.ManazerOkolia;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Simulation;
import OSPStat.Stat;

public class AgentOkolia extends Agent
{
	public static final int kPocetSkupin = 6;
	
	private PlanovacOtvoreniaRestauracie _planovacOtvoreniaRestauracie;
	private List< PlanovacPrichoduZakaznikov > _planovacePrichodov;
	
	private Stat _statCelkovyCasCakania;
	private List< SkupinaZakaznikov > _cakajuciPredOtvorenim;

	public AgentOkolia(int id, Simulation mySim, Agent parent, Stat statCelkovyCasCakania )
	{
		super(id, mySim, parent);
		
		new ManazerOkolia(Id.manazerOkolia, mySim, this);

		_planovacePrichodov = new LinkedList< PlanovacPrichoduZakaznikov >();
		for (int i = 0; i < kPocetSkupin; ++i)
		{
			_planovacePrichodov.add(new PlanovacPrichoduZakaznikov(Id.planovacPrichoduZakaznikov, mySim, this, i+1));
		}
		_planovacOtvoreniaRestauracie = new PlanovacOtvoreniaRestauracie(Id.planovacOtvoreniaRestauracie, mySim, this);

		addOwnMessage(Mc.novyZakaznik);
		addOwnMessage(Mc.odchodZakaznika);
		addOwnMessage(Mc.otvorenieRestauracie);
		
		_statCelkovyCasCakania = statCelkovyCasCakania;
		_cakajuciPredOtvorenim = new LinkedList< SkupinaZakaznikov >();
	}

	public List< PlanovacPrichoduZakaznikov > planovacePrichodov()
	{ return _planovacePrichodov; }
	
	public void zacniPlanovanieZakaznikov()
	{
		for (PlanovacPrichoduZakaznikov planovac : _planovacePrichodov)
		{
			Sprava sprava = new Sprava(mySim());
			sprava.setAddressee(planovac);
			manager().startContinualAssistant(sprava);
		}
		Sprava sprava = new Sprava(mySim());
		sprava.setAddressee(_planovacOtvoreniaRestauracie);
		manager().startContinualAssistant(sprava);
	}

	public int pocetObsluzenychZakaznikov()
	{ return ((ManazerOkolia)manager()).pocetObsluzenychZakaznikov(); }
	
	public int pocetNeobsluzenychZakaznikov()
	{ return ((ManazerOkolia)manager()).pocetNeobsluzenychZakaznikov(); }

	public int pocetVygenerovanychZakaznikov()
	{ return ((ManazerOkolia)manager()).pocetVygenerovanychZakaznikov(); }
	
	public Stat statCelkovyCasCakania()
	{ return _statCelkovyCasCakania; }
	
	public List< SkupinaZakaznikov > cakajuciPredOtvorenim()
	{ return _cakajuciPredOtvorenim; }
}
