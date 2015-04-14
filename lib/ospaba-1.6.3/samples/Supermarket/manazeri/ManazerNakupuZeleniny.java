package supermarket.manazeri;

import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerNakupuZeleniny extends ManazerOddeleniaSPracovnikom
{
	public ManazerNakupuZeleniny(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		super.processMessage(message);

		switch (message.code())
		{
		case Mc.nakupZeleninu:
			if (0 < ((Sprava)message).zakaznik().pocetZelenina()) // ak nakupuje aspon jednu polozku zeleniny
			{
				zacniObsluhu((Sprava)message);
			}
			else // ak nenakupuje ziadnu zeleninu
			{
				message.setCode(Mc.nakupZeleninyHotovy);
				response(message);
			}
		break;
			
		case Mc.finish: // procesNakupuZeleniny
			ukonciObsluhu((Sprava)message);
			
			message.setCode(Mc.nakupZeleninyHotovy);
			response(message);
		break;
		}
	}
}
