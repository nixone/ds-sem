package kontinualnyAsistenti;

import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.ErlangRNG;

public class ProcesPredexpedicnehoSpracovania extends ContinualAssistant
{
	private static ErlangRNG _erlang = new ErlangRNG(396d, 252d); // [s]

	public ProcesPredexpedicnehoSpracovania(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		
		switch (sprava.code())
		{
		case Mc.start:
			sprava.setCode(Mc.hold);
			hold(_erlang.sample() / 60d, sprava);
		break;

		case Mc.hold:
			sprava.rolka().setPripravenaNaExpedovanie(true);
			assistantFinished(sprava);
		break;
		}
	}
}
