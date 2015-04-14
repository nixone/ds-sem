package supermarket.manazeri;

import java.util.ArrayList;
import java.util.List;

import supermarket.agenti.AgentModelu;
import supermarket.entity.Zakaznik;
import supermarket.simulacia.Mc;
import supermarket.simulacia.SimulaciaSupermarketu;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerModelu extends Manager
{
	private List<Zakaznik> _obsluzenyZakaznici;
	
	public ManazerModelu(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		
		_obsluzenyZakaznici = new ArrayList<Zakaznik>();
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			message.setAddressee(((SimulaciaSupermarketu)mySim()).agentOkolia());
			message.setCode(Mc.init);
			notice(message);
			break;

		case Mc.prichodZakaznika:
			message.setAddressee(((SimulaciaSupermarketu)mySim()).agentSupermarketu());
			message.setSender(myAgent());
			message.setCode(Mc.obsluzZakaznika);
			request(message);
			break;

		case Mc.odchodZakaznika:
//			_obsluzenyZakaznici.add(((Sprava)message).zakaznik());
			
			int dlzkaFrontov = ((SimulaciaSupermarketu)mySim()).agentPokladne().pocetZakaznikovVoFrontoch();
			double casCakaniaZakaznika = ((Sprava)message).zakaznik().celkovyCasCakania();

			((AgentModelu)myAgent()).statistikaCelkovyCasCakania().addSample(casCakaniaZakaznika);
			((AgentModelu)myAgent()).statistikaDlzkaFrontov().addSample(dlzkaFrontov);
			break;
		}
	}
	
	public List<Zakaznik> obsluzenyZakaznici()
	{ return _obsluzenyZakaznici; }
}


//((SimulaciaSupermarketu)mySim()).agentNakupuZeleniny().pocetZakaznikovVoFrontoch()
//((SimulaciaSupermarketu)mySim()).agentNakupuMasoSyr().pocetZakaznikovVoFrontoch()