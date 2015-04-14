package manazeri;

import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerRestauracie extends Manager
{
	public ManazerRestauracie(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		// spravy su dorucene ako ciastocne adresne spravy, preto ich agent restauracie nemusi spracovat - su
		// automaticky dorucene od agenta restauracie agentom ktory ich vedia spracovat
		switch (message.code()) 
		{
		// pre pouzitie adresnych sprav: odkomentovat zakomentovany kod
		
//		case Mc.obsluhaZakaznika:
//			
//			sprava.setAddressee(((SimulaciaRestovracie)mySim()).agentJedalne());
//			request(message);
//			
//		break;
//			
//		case Mc.obsluhaZakaznikaUkoncena:
//			
//			response(message);
//			
//		break;
//
//		case Mc.pripravaJedla:
//			
//			sprava.setAddressee(((SimulaciaRestovracie)mySim()).agentKuchyne());
//			request(message);
//			
//		break;
//			
//		case Mc.pripravaJedlaUkoncena:
//			
//			response(message);
//			
//		break;
		}
	}
}
