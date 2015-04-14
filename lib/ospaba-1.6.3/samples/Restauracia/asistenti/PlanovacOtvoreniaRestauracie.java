package asistenti;

import simulacia.Config;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Scheduler;
import OSPABA.Simulation;

public class PlanovacOtvoreniaRestauracie extends Scheduler
{
	public PlanovacOtvoreniaRestauracie(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		switch (message.code())
		{
		case Mc.start:
			sprava.setCode(Mc.otvorenieRestauracie);
			hold(Config.casOtvoreniaRestauracie-Config.simStartTime, sprava);
		break;

		case Mc.otvorenieRestauracie:
			assistantFinished(sprava);
		break;
		}
	}
}
