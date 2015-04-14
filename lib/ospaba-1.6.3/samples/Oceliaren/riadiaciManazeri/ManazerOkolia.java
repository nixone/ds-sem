package riadiaciManazeri;

import kontinualnyAsistenti.PlanovacPrichodovRoliek;
import riadiaciAgenti.AgentOkolia;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerOkolia extends Manager
{
	private int _pocetVygenerovanychRoliek;

	public ManazerOkolia(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		
		_pocetVygenerovanychRoliek = 0;
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			for (PlanovacPrichodovRoliek planovac : myAgent().planovacePrichodovRoliek())
			{
				Sprava copy = new Sprava((Sprava)message);
				copy.setAddressee(planovac);
				startContinualAssistant(copy);
			}
		break;
		
		case Mc.finish: // planovacPrichodovRoliek
			message.setAddressee(mySim().findAgent(Id.agentModleu));
			message.setCode(Mc.prichodRolky);
			notice(message);
			
			++_pocetVygenerovanychRoliek;
		break;

		case Mc.odchodRolky:
			;
		break;
		}
	}
	
	@Override
	public AgentOkolia myAgent()
	{ return (AgentOkolia)super.myAgent(); }
	
	public int pocetVygenerovanychRoliek()
	{ return _pocetVygenerovanychRoliek; }
}
