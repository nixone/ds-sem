package kontinualnyAsistenti;

import simulacia.Mc;
import OSPABA.CommonAgent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ProcesPresunuTimuPracovnikov extends ContinualAssistant
{
	private double _casPresunu = 13;

	public ProcesPresunuTimuPracovnikov(int id, Simulation mySim, CommonAgent myAgent)
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
			hold(_casPresunu, message);
		break;
		
		case Mc.hold:
			assistantFinished(message);
		break;
		}
	}
}
