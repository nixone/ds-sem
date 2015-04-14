package asistenti;

import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Process;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ProcesPripravyJedla extends Process
{
	public ProcesPripravyJedla(int id, Simulation mySim, Agent myAgent)
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
			sprava.setCode(Mc.pripravaJedlaUkoncena);
			hold(sprava.pokrm().casPripravy(), sprava);
		break;

		case Mc.pripravaJedlaUkoncena:
			assistantFinished(sprava);	
		break;
		}	
	}
}
