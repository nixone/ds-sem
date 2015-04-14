package supermarket.manazeri;

import supermarket.agenti.AgentOddeleniaSPracovnikom;
import supermarket.entity.Pracovnik;
import supermarket.entity.Zakaznik;
import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public abstract class ManazerOddeleniaSPracovnikom extends Manager
{	
	public ManazerOddeleniaSPracovnikom(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}
	
	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		AgentOddeleniaSPracovnikom agent = (AgentOddeleniaSPracovnikom)myAgent();
		switch (message.code())
		{
		case Mc.odchodPracovnika:
			if (agent.pracovnici().size() > 1
				&& sprava.pracovnik().pocetZakaznikov() == 0
				&& sprava.pracovnik().pocetObsluzenychZakaznikov() == sprava.pocetObsluzenychZakaznikov())
			{
				agent.pracovnici().remove(sprava.pracovnik());
			}
			break;
		}
	}
	
	protected void zacniObsluhu(Sprava message)
	{
		AgentOddeleniaSPracovnikom agent = (AgentOddeleniaSPracovnikom)myAgent();
		
		agent.dotazNajkratsiFront().execute(message); // nastavy atribut 'pracovnik' spravy 'message' na pracovnika s najkratsim frontom
		Pracovnik pracovnik = message.pracovnik(); // pracovnik s najkratsim frontom
		
		if (pracovnik.obsluhovanyZakaznik() != null) // ak pracovnik pracuje, zakaznik je zaradeny do frontu
		{
			pracovnik.pridajZakaznikaDoFrontu(message.zakaznik());
			message.zakaznik().setCasZaradeniaDoFrontu(mySim().currentTime());
			
			agent.statistikaDlzkaFrontu().addSample(agent.pocetZakaznikovVoFrontoch());
			
			if (agent.novySposobPrace())
			{
				boolean potrebnyNovyPracovnik = false;
				if (agent.pracovnici().size() < agent.maxPocetPracovnikov())
				{
					potrebnyNovyPracovnik = true;
					for (Pracovnik p : agent.pracovnici())
					{
						if (p.dlzkaFrontu() < 3)
						{
							potrebnyNovyPracovnik = false;
							break;
						}
					}
				}
				if (potrebnyNovyPracovnik)
				{
					Pracovnik novyPracovnik = agent.pridajPracovnika();
					
					Sprava msgCopy = new Sprava(message);
					msgCopy.setPracovnik(novyPracovnik);
					message.setPocetObsluzenychZakaznikov(novyPracovnik.pocetObsluzenychZakaznikov());
					message.setAddressee(agent.planovacOdchoduPracovnika());
					message.setCode(Mc.start);
					
					startContinualAssistant(message);
				}
			}
		}
		else // ak pracovnik nepracuje, zacne obsluhu zakaznika
		{
			pracovnik.setObsluhovanyZakaznik(message.zakaznik());
			message.setAddressee(proces());
			startContinualAssistant(message);
			
			agent.statistikaCasCakania().addSample(0);
		}
	}
	
	protected void ukonciObsluhu(Sprava message)
	{
		AgentOddeleniaSPracovnikom agent = (AgentOddeleniaSPracovnikom)myAgent();
		Pracovnik pracovnik = message.pracovnik();
		
		pracovnik.zvisPocetObsluzenychZakaznikov();
		
		if (0 < pracovnik.dlzkaFrontu()) // ak nie je front prazdny, pracovnik zacne obsluhu dalsieho zakaznika
		{
			Sprava msgCopy = new Sprava(message);
			Zakaznik dalsiZakaznik = pracovnik.vyberZakaznikaZFrontu();
			pracovnik.setObsluhovanyZakaznik(dalsiZakaznik);

			msgCopy.setZakaznik(dalsiZakaznik); // nastavy zakaznika v msgCopy na zakaznika vybrateho z frontu
			msgCopy.setAddressee(proces());
			startContinualAssistant(msgCopy);
			
			// zber statistik
			double casCakania = mySim().currentTime() - dalsiZakaznik.casZaradeniaDoFrontu();
			agent.statistikaDlzkaFrontu().addSample(agent.pocetZakaznikovVoFrontoch());
			agent.statistikaCasCakania().addSample(casCakania);
			
			dalsiZakaznik.setCasZaradeniaDoFrontu(-1);
			dalsiZakaznik.zvisCelkovyCasCakania(casCakania);
		}
		else
		{
			pracovnik.setObsluhovanyZakaznik(null);
			
			if (agent.novySposobPrace())
			{
				if (1 < agent.pracovnici().size())
				{
					Sprava msgCopy = new Sprava(message);
					msgCopy.setPocetObsluzenychZakaznikov(pracovnik.pocetObsluzenychZakaznikov());
					msgCopy.setAddressee(agent.planovacOdchoduPracovnika());
					msgCopy.setCode(Mc.start);
					
					startContinualAssistant(msgCopy);
				}
			}
		}
	}
	
	public ContinualAssistant proces()
	{ return ((AgentOddeleniaSPracovnikom)myAgent()).proces(); }
}
