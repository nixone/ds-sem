package supermarket.manazeri;

import java.util.ArrayList;
import java.util.List;

import supermarket.agenti.AgentNakupu;
import supermarket.entity.Zakaznik;
import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerNakupu extends Manager
{
	private List<Zakaznik> _zakaznici;
	
	public ManazerNakupu(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		
		_zakaznici = new ArrayList<Zakaznik>();
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.nakupOstatne:
			_zakaznici.add(((Sprava)message).zakaznik());
			
			message.setCode(Mc.start);
			message.setAddressee(((AgentNakupu)myAgent()).procesNakupu());
			startContinualAssistant(message);
			break;

		case Mc.finish:	// procesNakupu
			_zakaznici.remove((((Sprava)message).zakaznik()));
			
			message.setCode(Mc.nakupOstatneHotovy);
			response(message);
			break;
		}
	}
	
	public List<Zakaznik> zakaznici()
	{
		return _zakaznici;
	}
}