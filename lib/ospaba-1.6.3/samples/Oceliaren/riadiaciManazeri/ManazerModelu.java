package riadiaciManazeri;

import entity.Rolka;
import simulacia.Config;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import simulacia.Stanovisko;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerModelu extends Manager
{
	public ManazerModelu(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			message.setAddressee(mySim().findAgent(Id.agentOkolia));
			notice(message);
			
			Sprava copy = new Sprava((Sprava)message);
			copy.setAddressee(mySim().findAgent(Id.agentOceliarne));
			notice(copy);
			
			naplnSkladyRolkami();
		break;
		
		case Mc.prichodRolky:
			message.setAddressee(mySim().findAgent(Id.agentOceliarne));
			message.setCode(Mc.spracovanieRolky);
			request(message);
		break;
		
		case Mc.spracovanieRolkyDokoncene:
			message.setAddressee(mySim().findAgent(Id.agentOkolia));
			message.setCode(Mc.odchodRolky);
			notice(message);
		break;
		}
	}
	
	private void naplnSkladyRolkami()
	{
		Rolka rolka;
		int [] idSkladov = { Id.sklad1, Id.sklad2, Id.sklad3, Id.sklad4 };
		Stanovisko [] stanoviska = { Stanovisko.S1, Stanovisko.S2, Stanovisko.S3, Stanovisko.S3 };
		
		for (int i = 0; i < idSkladov.length; ++i)
		{
			int idSkladu = idSkladov[i];

			for (int j = 0; j < Config.poctyOpracovanychRoliekVSkladoch[i]; ++j)
			{
				boolean pripravenaNaExpedovanie = (idSkladu == Id.sklad4 || idSkladu == Id.sklad3);
				rolka = new Rolka(mySim(), true, pripravenaNaExpedovanie);
				rolka.setStanovisko(stanoviska[i]);
				rolka.setCielovySklad(idSkladu);
				posliInicializacnuRolku(rolka, idSkladu);
			}
			for (int j = 0; j < Config.poctyNeopracovanychRoliekVSkladoch[i]; ++j)
			{
				rolka = new Rolka(mySim(), idSkladu == Id.sklad4, false);
				rolka.setStanovisko(stanoviska[i]);
				rolka.setCielovySklad(idSkladu);
				posliInicializacnuRolku(rolka, idSkladu);
			}
		}
	}
	
	private void posliInicializacnuRolku(Rolka rolka, int idSkladu)
	{
		Sprava sprava = new Sprava(mySim());
		sprava.setAddressee(mySim().findAgent(Id.agentOceliarne));
		sprava.setCode(Mc.initRolkaSkladu);
		sprava.setRolka(rolka);
		sprava.setIdSkladu(idSkladu);
		request(sprava);
	}
}
