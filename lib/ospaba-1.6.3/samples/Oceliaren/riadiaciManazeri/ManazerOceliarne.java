package riadiaciManazeri;

import java.util.LinkedList;
import java.util.List;

import entity.Rolka;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerOceliarne extends Manager
{
	public ManazerOceliarne(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		Rolka rolka = sprava.rolka();

		switch (sprava.code())
		{
		case Mc.init:
			sprava.setAddressee(mySim().findAgent(Id.agentDopravnikov));
			notice(sprava);	
		break;
		
		case Mc.initRolkaSkladu:
			sprava.setAddressee(mySim().findAgent(Id.agentSkladov));
			request(sprava);
		break;
		
		case Mc.spracovanieRolky:		
			pridanieRolkyDoDopravnika(sprava);
		break;
		
		case Mc.pridanieRolkyDoDopravnikaDokoncene:			
			if (! rolka.jePripravenaNaExpedovanie())
			{
				if (sprava.rolka().cielovySklad() == Id.sklad3)
				{
					pridelenieVozidla(new Sprava(sprava));
				}
				else
				{
					pridelenieMiestaVSklade(new Sprava(sprava));
				}
			}
		break;
		
		case Mc.pridelenieMiestaVSkladeDokoncene:			
			pridelenieZeriavu(sprava);
		break;

		case Mc.opracovanieRolkyDokoncene:			
			if (rolka.sklad().id() == Id.sklad1 || rolka.sklad().id() == Id.sklad2)
			{
				pridelenieVozidla(sprava);
			}
			else
			{
				pridelenieZeriavu(sprava);
			}	
		break;
				
		case Mc.pridelenieVozidlaDokoncene:			
			if (sprava.rolka().stanovisko() != sprava.vozidlo().stanovisko())
			{
				presunVozidla(sprava); // presun vozidla k rolke
			}
			else // nalozenie rolky
			{
				pridelenieZeriavu(sprava);
			}
		break;
			
		case Mc.presunVozidlaDokonceny:			
			if (rolka.vozidlo() != null) // vylozenie rolky
			{
				pridelenieMiestaVSklade(sprava);
			}
			else 
			{
				pridelenieZeriavu(sprava); // nalozenie rolky
			}
		break;
		
		case Mc.pridelenieZeriavuDokoncene:	
			prelozenieRolky(sprava);
		break;
			
		case Mc.prelozenieRolkyDokoncene:		
			if (rolka.dopravnik() != null) // rolka je na dopravniku
			{
				if (rolka.dopravnik().jeVstupnyDopravnik())
				{
					if (rolka.cielovySklad() == Id.sklad3) // prelozenie na vozidlo
					{				
						presunVozidla(new Sprava(sprava));
					}
					else // prelozenie do skladu
					{
						opracovanieRolky(new Sprava(sprava));
					}
					odstranenieRolkyZDopravnika(new Sprava(sprava));
				}
			}
			else if (rolka.sklad() != null) // rolka je v sklade
			{
				if (rolka.jePripravenaNaExpedovanie()) // rolka prelozena na vystupny dopravnik
				{
					pridanieRolkyDoDopravnika(new Sprava(sprava));
				}
				else // rolka prelozena na vozidlo
				{					
					presunVozidla(new Sprava(sprava));
				}
				odstranenieRolkyZoSkladu(new Sprava(sprava));
			}
			else if (rolka.vozidlo() != null) // rolka je na vozidle
			{
				uvolnenieVozidla(new Sprava(sprava));
				opracovanieRolky(new Sprava(sprava));
			}			
			uvolnenieZeriavu(new Sprava(sprava));
		break;
		
		case Mc.odchodRolky:
			spracovanieRolkyDokoncene(new Sprava(sprava));
			spracovane.add(sprava);
		break;
		
		case Mc.entrustDA:
			myAgent().addDynamicAgent(sprava.vozidlo());
			sprava.setAddressee(mySim().findAgent(Id.agentZeriavov));
			entrustDynamicAgent(sprava);
		break;
		
		case Mc.returnDA:			
			myAgent().addDynamicAgent(sprava.vozidlo());
			returnDynamicAgent(sprava);
		break;
		}
	}
	
	static List<Sprava> spracovane = new LinkedList<>();
	
	private void pridelenieVozidla(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentVozidiel));
		sprava.setCode(Mc.pridelenieVozidla);
		request(sprava);
	}
	
	private void presunVozidla(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentVozidiel));
		sprava.setCode(Mc.presunVozidla);
		request(sprava);
	}
	
	private void uvolnenieVozidla(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentVozidiel));
		sprava.setCode(Mc.uvolnenieVozidla);
		notice(sprava);
	}
	
	private void pridelenieZeriavu(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentZeriavov));
		sprava.setCode(Mc.pridelenieZeriavu);
		request(sprava);
	}
	
	private void prelozenieRolky(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentZeriavov));
		sprava.setCode(Mc.prelozenieRolky);
		request(sprava);
	}
	
	private void uvolnenieZeriavu(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentZeriavov));
		sprava.setCode(Mc.uvolnenieZeriavu);
		notice(sprava);
	}
	
	private void pridanieRolkyDoDopravnika(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentDopravnikov));
		sprava.setCode(Mc.pridanieRolkyDoDopravnika);
		request(sprava);
	}
	
	private void odstranenieRolkyZDopravnika(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentDopravnikov));
		sprava.setCode(Mc.odstranenieRolkyZDopravnika);
		notice(sprava);
	}
	
	private void pridelenieMiestaVSklade(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentSkladov));
		sprava.setCode(Mc.pridelenieMiestaVSklade);
		request(sprava);
	}
	
	private void opracovanieRolky(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentSkladov));
		sprava.setCode(Mc.opracovanieRolky);
		request(sprava);
	}
	
	private void odstranenieRolkyZoSkladu(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentSkladov));
		sprava.setCode(Mc.odstranenieRolkyZoSkladu);
		notice(sprava);
	}
	
	private void spracovanieRolkyDokoncene(Sprava sprava)
	{
		sprava.setAddressee(mySim().findAgent(Id.agentModleu));
		sprava.setCode(Mc.spracovanieRolkyDokoncene);
		response(sprava);
	}
}
