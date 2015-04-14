package supermarket.asistenti;

import supermarket.entity.Zakaznik;
import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.ExponentialRNG;

public class PlanovacPrichoduZakaznika extends ContinualAssistant
{
	private static ExponentialRNG _exp = new ExponentialRNG(60/11.0); // 500 100

	public PlanovacPrichoduZakaznika(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:

			message.setCode(Mc.novyZakaznik);
			hold(_exp.sample(), message);

			break;

		case Mc.novyZakaznik:

			Sprava msg = new Sprava((Sprava)message);
			hold(_exp.sample(), msg);
			
			((Sprava)message).setZakaznik(new Zakaznik(mySim()));
			assistantFinished(message);
	
			break;
		}
	}
}