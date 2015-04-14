package simulacia;

import agenti.AgentCerpacejStanice;
import agenti.AgentModelu;
import agenti.AgentOkolia;
import OSPABA.Simulation;

public class SimulaciaCerpacejStanice extends Simulation
{
	private AgentModelu _agentModelu;
	private AgentOkolia _agentOkolia;
	private AgentCerpacejStanice _agentCerpacejStanice;

	public SimulaciaCerpacejStanice()
	{
		_agentModelu = new AgentModelu(Id.agentModelu, this, null);
		_agentOkolia = new AgentOkolia(Id.agentOkolia, this, _agentModelu);
		_agentCerpacejStanice = new AgentCerpacejStanice(Id.agentCerpacejStanice, this, _agentModelu);
	}
	
	public AgentModelu agentModelu()
	{ return _agentModelu; }
	
	public AgentOkolia agentOkolia()
	{ return _agentOkolia; }
	
	public AgentCerpacejStanice agentCerpacejStanice()
	{ return _agentCerpacejStanice; }
}
