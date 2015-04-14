package supermarket.manazeri;

import supermarket.simulacia.Mc;
import supermarket.simulacia.SimulaciaSupermarketu;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerSupermarketu extends Manager
{
	public ManazerSupermarketu(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.obsluzZakaznika:
			((Sprava)message).zakaznik().setCasCasUkonceniaObsluhy(mySim().currentTime());

			message.setAddressee(((SimulaciaSupermarketu)myAgent().mySim()).agentNakupu());
			message.setSender(myAgent());
			message.setCode(Mc.nakupOstatne);
			request(message);
			break;

		case Mc.nakupOstatneHotovy:
			message.setAddressee(((SimulaciaSupermarketu)myAgent().mySim()).agentNakupuZeleniny());
			message.setSender(myAgent());
			message.setCode(Mc.nakupZeleninu);
			request(message);				
			break;
			
		case Mc.nakupZeleninyHotovy:			
			message.setAddressee(((SimulaciaSupermarketu)myAgent().mySim()).agentNakupuMasoSyr());
			message.setSender(myAgent());
			message.setCode(Mc.nakupMasoSyr);
			request(message);
			break;
			
		case Mc.nakupMasoSyrHotovy:
			message.setAddressee(((SimulaciaSupermarketu)myAgent().mySim()).agentPokladne());
			message.setSender(myAgent());
			message.setCode(Mc.platenie);
			request(message);	
			break;
			
		case Mc.pladbaHotova:
			((Sprava)message).zakaznik().setCasCasUkonceniaObsluhy(mySim().currentTime());

			message.setCode(Mc.odchodZakaznika);
			response(message);
			break;
		}
	}
}