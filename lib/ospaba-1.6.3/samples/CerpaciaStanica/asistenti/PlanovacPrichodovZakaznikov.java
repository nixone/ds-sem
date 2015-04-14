package asistenti;

import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Scheduler;
import OSPABA.Simulation;
import OSPRNG.ExponentialRNG;

public class PlanovacPrichodovZakaznikov extends Scheduler
{
	private static ExponentialRNG _exp = new ExponentialRNG(100d);

	public PlanovacPrichodovZakaznikov(int id, Simulation mySim, Agent myAgent)
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
			MessageForm copy = message.createCopy();
			hold(_exp.sample(), copy);

			assistantFinished(message);
		break;
		}
	}

}