package kontinualnyAsistenti;

import simulacia.Config;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Scheduler;
import OSPABA.Simulation;

public class PlanovacOdchoduRoliek extends Scheduler
{
	private final double _intervalOdchodovRoliek = 9 + 5 / 6d;

	public PlanovacOdchoduRoliek(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			message.setCode(Mc.hold);
		break;
		
		case Mc.hold:
			Sprava copy = new Sprava((Sprava)message);			
			assistantFinished(copy);
		break;
		}
		hold(_intervalOdchodovRoliek / Config.intenzitaVstupnehoToku, message);
	}
}
