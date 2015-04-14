package agenti;

import asistenti.PlanovacPrichodovZakaznikov;
import manazeri.ManazerOkolia;
import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.Simulation;

public class AgentOkolia extends Agent
{
	private ContinualAssistant _planovacPrichodovZakaznikov;

	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);

		new ManazerOkolia(Id.manazerOkolia, mySim, this);
		
		_planovacPrichodovZakaznikov = //new Scheduler(Id.planovacPrichodovZakaznikov, mySim, this, new ExponentialRNG(100d));
		new PlanovacPrichodovZakaznikov(Id.planovacPrichodovZakaznikov, mySim, this);

		addOwnMessage(Mc.init);
		addOwnMessage(Mc.novyZakaznik);
		addOwnMessage(Mc.odchodZakaznika);
	}
	
	public void zacniPlanovanieZakaznikov()
	{
		((ManazerOkolia)manager()).zacniPlanovanieZakaznikov();
	}
	
	public ContinualAssistant planovacPrichodovZakaznikov()
	{ return _planovacPrichodovZakaznikov; }
}
