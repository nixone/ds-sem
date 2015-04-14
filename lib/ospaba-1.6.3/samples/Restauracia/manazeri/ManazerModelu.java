package manazeri;

import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

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
		case Mc.prichodZakaznika:
			message.setCode(Mc.obsluhaZakaznika);
			message.setAddressee(mySim().findAgent(Id.agentRestovracie));			
			request(message);
		break;
			
		case Mc.obsluhaZakaznikaUkoncena:
			int code = Mc.odchodZakaznika;
			message.setCode(code);
			message.setAddressee(mySim().yellowPages().findFirstAgent(code));
			notice(message);			
		break;
		}
	}
}
