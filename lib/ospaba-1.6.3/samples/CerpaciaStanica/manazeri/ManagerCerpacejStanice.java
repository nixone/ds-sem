package manazeri;

import agenti.AgentCerpacejStanice;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManagerCerpacejStanice extends Manager
{
	private boolean isWorking;
	
	public ManagerCerpacejStanice(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		
		isWorking = false;
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.obsluhaZakaznika:
			if (isWorking)
			{
				((Sprava)message).setZaciatokCakania(mySim().currentTime());
				myAgent().frontZakaznikov().enqueue(message);
			}
			else
			{
				startWork(message);
			}
		break;
			
		case Mc.finish: // procesObsluhyZakaznika
			isWorking = false;
			myAgent().casCakania().addSample(((Sprava)message).celkoveCakanie());
			
			if (0 < myAgent().frontZakaznikov().size())
			{
				Sprava nextMessage = (Sprava)myAgent().frontZakaznikov().dequeue();
				nextMessage.setCelkoveCakanie(mySim().currentTime() - nextMessage.zaciatokCakania());
				startWork(nextMessage);
			}
			
			message.setCode(Mc.obsluhaZakaznikaHotova);
			response(message);
		break;
		}		
	}
	
	private void startWork(MessageForm message)
	{
		isWorking = true;
//		message.setAddressee(myAgent().procesObsluhyZakaznika());
		message.setAddressee(myAgent().findAssistant(Id.procesObsluhyZakaznika));
		startContinualAssistant(message);		
	}

	@Override
	public AgentCerpacejStanice myAgent()
	{ return (AgentCerpacejStanice)(super.myAgent()); }
}
