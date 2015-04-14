package manazeri;

import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import simulacia.Id;
import simulacia.Mc;

public class ManazerModelu extends Manager
{	
	public ManazerModelu(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			message.setAddressee(mySim().findAgent(Id.agentOkolia));
			notice(message);
		break;

		case Mc.prichodZakaznika:
			message.setCode(Mc.obsluhaZakaznika);
			message.setAddressee(mySim().findAgent(Id.agentCerpacejStanice));
			
			request(message);
		break;

		case Mc.obsluhaZakaznikaHotova:
			message.setCode(Mc.odchodZakaznika);
			message.setAddressee(mySim().findAgent(Id.agentOkolia));
			
			notice(message);
		break;
		}
	}

}
