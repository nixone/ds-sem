package kontinualnyAsistenti;

import entity.Rolka;
import simulacia.Config;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Scheduler;
import OSPABA.Simulation;

public abstract class PlanovacPrichodovRoliek extends Scheduler
{
	public PlanovacPrichodovRoliek(int id, Simulation mySim, CommonAgent myAgent)
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
			copy.setRolka(novaRolka());
			assistantFinished(copy);
		break;
		}
		hold(exp() / Config.intenzitaVstupnehoToku, message);
	}
	
	public abstract double exp();
	public abstract Rolka novaRolka();
}
