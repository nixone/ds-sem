package agenti;

import manazeri.ManagerCerpacejStanice;
import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import asistenti.ProcesObsluhyZakaznika;

public class AgentCerpacejStanice extends Agent
{
	private ContinualAssistant _procesObsluhyZakaznika;
	
	private SimQueue< MessageForm > _frontZakaznikov;
	private Stat _casCakaniaStat;

	public AgentCerpacejStanice(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);

		new ManagerCerpacejStanice(Id.manazerCerpacejStanice, mySim, this);
		
		_procesObsluhyZakaznika = new ProcesObsluhyZakaznika(Id.procesObsluhyZakaznika, mySim, this);
		_frontZakaznikov = new SimQueue<>(new WStat(mySim));
		_casCakaniaStat = new Stat();
		
		addOwnMessage(Mc.obsluhaZakaznika);
		addOwnMessage(Mc.koniecObsluhy);
	}
	
	public Stat casCakania()
	{ return _casCakaniaStat; }
	
	public WStat dlzkaFrontu()
	{ return _frontZakaznikov.lengthStatistic(); }
	
	public SimQueue<MessageForm> frontZakaznikov()
	{ return _frontZakaznikov; }
	
	public ContinualAssistant procesObsluhyZakaznika()
	{ return _procesObsluhyZakaznika; }
}
