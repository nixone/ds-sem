package asistenti;

import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.ExponentialRNG;
import OSPABA.Process;

public class ProcesObsluhyZakaznika extends Process
{
	private static ExponentialRNG _exp = new ExponentialRNG(45d);

	public ProcesObsluhyZakaznika(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			message.setCode(Mc.koniecObsluhy);
			hold(_exp.sample(), message);
		break;
			
		case Mc.koniecObsluhy:
			assistantFinished(message);	
		break;
		}
	}
}
