package riadiaciManazeri;

import dynamickyAgenti.AgentVozidla;
import entity.Rolka;
import riadiaciAgenti.AgentVozidiel;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import simulacia.Stanovisko;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerVozidiel extends Manager
{
	public ManazerVozidiel(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		Rolka rolka = sprava.rolka();
		
		switch (message.code())
		{
		case Mc.pridelenieVozidla:
			myAgent().pridajRolkuDoFrontu(sprava);
			
			if (myAgent().jeVolneVozidlo())
			{
				pridelenieVozidla(sprava);
			}
		break;
		
		case Mc.presunVozidla:
			AgentVozidla vozidlo = sprava.vozidlo();

			if (vozidlo.stanovisko() != rolka.stanovisko()) // presun vozidla k rolke
			{
				sprava.setCiel(rolka.stanovisko());
			}
			else // prevoz rolky
			{
				rolka.setVozidlo(vozidlo);
				sprava.setCiel(Stanovisko.S3);
			}
			sprava.setAddressee(vozidlo);
			goalDynamicAgent(sprava);
		break;
		
		case Mc.done: // presunVozidla
			if (rolka.vozidlo() != null)
			{
				sprava.rolka().setStanovisko(rolka.vozidlo().stanovisko());
			}
			sprava.setCode(Mc.presunVozidlaDokonceny);
			response(sprava);
		break;
		
		case Mc.transfer:
			sprava.setAddressee(mySim().findAgent(Id.agentOceliarne));
			entrustDynamicAgent(sprava);	
		break;
			
		case Mc.returnDA:
			myAgent().addDynamicAgent(sprava.vozidlo());
		break;
		
		case Mc.uvolnenieVozidla:
			rolka.vozidlo().setVolne(true);
			rolka.setVozidlo(null);
			
			if (myAgent().cakajuRolkyNaVozidlo())
			{
				pridelenieVozidla(sprava);
			}
		break;
		}
	}
	
	@Override
	public AgentVozidiel myAgent()
	{ return (AgentVozidiel)super.myAgent(); }
	
	private void pridelenieVozidla(Sprava sprava)
	{
		sprava = new Sprava(sprava);
		myAgent().pravidloVstupnychRoliek().execute(sprava); // vyberie ktora rolka ma byt prevezena
		
		myAgent().dotazVolneVozidlo().execute(sprava); // vyberie najbilssie volne vozidlo k rolke ktora ma byt spracovana
		
		if (sprava.vozidlo() != null)
		{
			sprava.vozidlo().setVolne(false);
			
			sprava.setCode(Mc.pridelenieVozidlaDokoncene);
			response(sprava);
		}
		else // ziadne vozidlo nie je volne
		{
			myAgent().pridajRolkuDoFrontu(sprava);
		}
	}
}
