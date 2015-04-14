package kontinualnyAsistenti;

import simulacia.Mc;
import OSPABA.CommonAgent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.UniformContinuousRNG;

public class ProcesPrelozeniaRolky extends ContinualAssistant
{
	private static UniformContinuousRNG _uniform = new UniformContinuousRNG(15d / 60d, 69d / 60d); // [min]

	public ProcesPrelozeniaRolky(int id, Simulation mySim, CommonAgent myAgent)
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
			hold(_uniform.sample(), message);
		break;
			
		case Mc.hold:
			assistantFinished(message);
		break;
		}
	}
}
