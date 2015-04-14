package dynamickyManazeri;

import dynamickyAgenti.AgentVozidla;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.DynamicAgent;
import OSPABA.DynamicAgentManager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerVozidla extends DynamicAgentManager
{
	public ManazerVozidla(int id, Simulation mySim, DynamicAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		
		switch (sprava.code())
		{
		case Mc.goal:	
			myAgent().setCasZaciatkuPrace(mySim().currentTime());
			myAgent().setStoji(false);
			sprava.setAddressee(myAgent().procesPresunu());
			startContinualAssistant(sprava);
		break;

		case Mc.finish: // procesPresunu
			Sprava copy = new Sprava(sprava);
			transfer(copy);

			myAgent().zvisCelkovyCasPrace(mySim().currentTime() - myAgent().casZaciatkuPrace());
			myAgent().setStoji(true);
			done(sprava);
		break;
		}
	}

	@Override
	public AgentVozidla myAgent()
	{ return (AgentVozidla)super.myAgent(); }
}
