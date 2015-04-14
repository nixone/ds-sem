package supermarket.manazeri;

import supermarket.agenti.AgentOkolia;
import supermarket.simulacia.Mc;
import supermarket.simulacia.SimulaciaSupermarketu;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerOkolia extends Manager
{
	public ManazerOkolia(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			message.setAddressee(((AgentOkolia)myAgent()).planovacPrichoduZakaznika());
			startContinualAssistant(message);   // nastavy kod spravy na start 
		break;

		case Mc.finish: // planovacPrichoduZakaznika
			((AgentOkolia)myAgent()).incPocetZakaznikov();
			
			message.setAddressee(((SimulaciaSupermarketu)mySim()).agentModelu());
			message.setCode(Mc.prichodZakaznika);
			notice(message);
		break;
		}
	}
}