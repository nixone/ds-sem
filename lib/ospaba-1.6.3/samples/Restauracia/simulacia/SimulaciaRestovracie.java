package simulacia;

import agenti.AgentJedalne;
import agenti.AgentKuchyne;
import agenti.AgentModelu;
import agenti.AgentOkolia;
import agenti.AgentRestauracie;
import OSPABA.Simulation;
import OSPStat.Stat;

public class SimulaciaRestovracie extends Simulation
{
	private int _id;
	
	private AgentModelu _agentModelu;
	private AgentOkolia _agentOkolia;
	private AgentRestauracie _agentRestovracie;
	private AgentKuchyne _agentKuchyne;
	private AgentJedalne _agentJedalne;
	
	public SimulaciaRestovracie(int id, int pocetCasnikov, int pocetKucharov, Stat statCelkovyCasCakania)
	{
		_id = id;

		_agentModelu = new AgentModelu(Id.agentModelu, this, null);
		_agentOkolia = new AgentOkolia(Id.agentOkolia, this, _agentModelu, statCelkovyCasCakania);
		_agentRestovracie = new AgentRestauracie(Id.agentRestovracie, this, _agentModelu);
		_agentKuchyne = new AgentKuchyne(Id.agentKuchyne, this, _agentRestovracie, pocetKucharov);
		_agentJedalne = new AgentJedalne(Id.agentJedalne, this, _agentRestovracie, pocetCasnikov);
	}
	
	public int id()
	{ return _id; }

	public AgentModelu agentModelu()
	{ return _agentModelu; }

	public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public AgentRestauracie agentRestovracie()
	{ return _agentRestovracie; }

	public AgentKuchyne agentKuchyne()
	{ return _agentKuchyne; }

	public AgentJedalne agentJedalne()
	{ return _agentJedalne; }
}
