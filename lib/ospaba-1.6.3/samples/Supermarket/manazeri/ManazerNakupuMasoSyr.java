package supermarket.manazeri;

import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerNakupuMasoSyr extends ManazerOddeleniaSPracovnikom
{
	public ManazerNakupuMasoSyr(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		super.processMessage(message);

		switch (message.code())
		{
		case Mc.nakupMasoSyr:
			if (0 < ((Sprava)message).zakaznik().pocetMasoASyr())
			{
				zacniObsluhu((Sprava)message);
			}
			else
			{
				message.setCode(Mc.nakupMasoSyrHotovy);
				response(message);
			}
			break;
			
		case Mc.finish: // procesNakupu
			ukonciObsluhu((Sprava)message);

			message.setCode(Mc.nakupMasoSyrHotovy);
			response(message);
			break;
		}
	}
}
