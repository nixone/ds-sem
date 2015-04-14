package supermarket.asistenti;

import supermarket.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class PlanovacOdchoduPracovnika extends ContinualAssistant
{
	// doba necinnosti, po ktorej pracovnik odchadza
	private final int _cas = 3 * 60;
	
	public PlanovacOdchoduPracovnika(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:

			hold(_cas, message);

			break;

		case Mc.finish:				

			message.setCode(Mc.odchodPracovnika);
			assistantFinished(message);

			break;
		}
	}
}
