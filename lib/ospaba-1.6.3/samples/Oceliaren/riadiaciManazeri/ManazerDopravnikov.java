package riadiaciManazeri;

import entity.Dopravnik;
import riadiaciAgenti.AgentDopravnikov;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerDopravnikov extends Manager
{
	public ManazerDopravnikov(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		Dopravnik dopravnik;
		
		switch (sprava.code())
		{
		case Mc.init:
			sprava.setAddressee(myAgent().planovacOdchoduRoliek());
			startContinualAssistant(sprava);
		break;
		
		case Mc.pridanieRolkyDoDopravnika:
			myAgent().dotazVyberDopravnik().execute(sprava);
			int idDopravniku = (int)sprava.msgResult();
			
			dopravnik = myAgent().dopravnik(idDopravniku);
			dopravnik.pridajRolku(sprava); // throws PrekrocenaKapacitaException
			sprava.rolka().setDopravnik(dopravnik);
			
			sprava.setCode(Mc.pridanieRolkyDoDopravnikaDokoncene);
			response(sprava);
		break;
		
		case Mc.odstranenieRolkyZDopravnika:
			sprava.rolka().dopravnik().odstranRolku(sprava); // throws DopravnikPrazdnyException
			sprava.rolka().setDopravnik(null);
		break;
		
		case Mc.finish: // planovac odchodu roliek
			dopravnik = myAgent().dopravnik(Id.dopravnik3);
			
			if (! dopravnik.jePrazdny())
			{
				sprava = dopravnik.odstranRolku();
	
				sprava.setAddressee(mySim().findAgent(Id.agentOceliarne));
				sprava.setCode(Mc.odchodRolky);
				notice(sprava);
			}
		break;
		}
	}

	@Override
	public AgentDopravnikov myAgent()
	{ return (AgentDopravnikov)super.myAgent(); }
}
