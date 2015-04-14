package supermarket.manazeri;

import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerPokladne extends ManazerOddeleniaSPracovnikom
{
	public ManazerPokladne(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		super.processMessage(message);

		switch (message.code())
		{
		case Mc.platenie:	
			zacniObsluhu((Sprava)message); 
		break;
			
		case Mc.finish: // procesPlatenia
			ukonciObsluhu((Sprava)message);
			
			message.setCode(Mc.pladbaHotova);
			response(message);
		break;
		}	
	}
}
