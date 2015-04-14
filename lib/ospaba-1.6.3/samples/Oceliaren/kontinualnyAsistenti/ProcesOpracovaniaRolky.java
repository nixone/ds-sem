package kontinualnyAsistenti;

import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.TriangularRNG;

public class ProcesOpracovaniaRolky extends ContinualAssistant
{
	private static TriangularRNG _tria = new TriangularRNG(1.8, 10.2, 52.2); // [min]

	public ProcesOpracovaniaRolky(int id, Simulation mySim, CommonAgent myAgent)
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
			
			message.setCode(Mc.hold);
			hold(_tria.sample(), sprava);
		
		break;

		case Mc.hold:
			
			sprava.rolka().sklad().opracujRolku(sprava);
			if (sprava.rolka().sklad().id() == Id.sklad3)
			{
				sprava.rolka().setPripravenaNaExpedovanie(true);
			}
//			else
//			{
//				sprava.rolka().setCielovySklad(Id.sklad4);
//			}
			assistantFinished(sprava);
			
		break;
		}
	}
}
