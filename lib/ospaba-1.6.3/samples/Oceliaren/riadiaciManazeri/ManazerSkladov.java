package riadiaciManazeri;

import entity.Rolka;
import entity.Sklad;
import riadiaciAgenti.AgentSkladov;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import simulacia.Stanovisko;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPExceptions.SimException;

public class ManazerSkladov extends Manager
{
	public ManazerSkladov(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava) message;
		Rolka rolka = sprava.rolka();
		Sklad sklad;
		SimQueue< Sprava > front = null;
		if (sprava.rolka() != null)
		{
			sklad = myAgent().sklad(idSkladu(sprava));
			front = myAgent().rolkyCakajuceNaOpracovanie(sklad.id());
		}

		switch (sprava.code())
		{
		case Mc.initRolkaSkladu:			
			sklad = myAgent().sklad(sprava.idSkladu());
			sprava.setIdSkladu(sklad.id());
			if (rolka.jeOpracovana() && sklad.id() != Id.sklad4 || rolka.jePripravenaNaExpedovanie())
			{
				rolka.setSklad(sklad);
				sklad.rolky().enqueue(sprava);
				
				sprava.setCode(Mc.opracovanieRolkyDokoncene);
				response(sprava);
			}
			else
			{				
				sklad.pridajRolku(sprava);
				sprava.setCode(Mc.opracovanieRolky);
				notice(sprava);
			}
		break;
		
		case Mc.pridelenieMiestaVSklade:
			sklad = myAgent().sklad(idSkladu(sprava));
			sprava.setIdSkladu(sklad.id());
			
			if (sklad.jeVolneMiesto())
			{
				pridelenieMiestaDokoncene(sprava);
			}
			else
			{
				myAgent().rolkyCakajuceNaPridelenieMiesta(sklad.id()).enqueue(sprava);
			}
		break;

		case Mc.opracovanieRolky:
			opracovanieRolky(sprava);
		break;
		
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.procesOpracovaniaRolky:
				Sprava copy = new Sprava(sprava);

				sprava.timPracovnikov().setPracuje(false);
				sprava.timPracovnikov().setRolka(null);
				sprava.setTimPracovnikov(null);
				
				sprava.setCode(Mc.opracovanieRolkyDokoncene);
				response(sprava);
				
				copy.setAddressee(myAgent().findAssistant(Id.pravidloPresunuTimov));
				execute(copy);
				
				if (copy.msgResult() == 1) // presun timu
				{
					copy.setRolka(null);
					copy.timPracovnikov().setPresuvaSa(true);
					copy.setAddressee(myAgent().findAssistant(Id.procesPresunuTimuPracovnikov));
					startContinualAssistant(copy);
				}
				if (0 < front.size()) // opracovanie dalsej rolky
				{
					opracovanieRolky(front.dequeue());
				}
			break;
			
			case Id.procesPredexpedicnehoSpracovaniaRolky:
				sprava.setCode(Mc.opracovanieRolkyDokoncene);
				response(sprava);
			break;
			
			case Id.procesPresunuTimuPracovnikov:
				sklad = myAgent().sklad(sprava.idSkladu());
				sprava.timPracovnikov().setSklad(sklad);
				sprava.timPracovnikov().setPresuvaSa(false);
				front = myAgent().rolkyCakajuceNaOpracovanie(sklad.id());

				if (0 < front.size())
				{
					opracovanieRolky(front.dequeue());
				}
			break;
			}
		break;
		
		case Mc.odstranenieRolkyZoSkladu:
			sklad = sprava.rolka().sklad();			
			sklad.odstranRolku(sprava);
			sprava.rolka().setSklad(null);
			
			if (! myAgent().rolkyCakajuceNaPridelenieMiesta(sklad.id()).isEmpty())
			{
				assert(sklad.pocetRoliek() < sklad.kapacita());
				sprava = myAgent().rolkyCakajuceNaPridelenieMiesta(sklad.id()).dequeue();
				pridelenieMiestaDokoncene(sprava);
			}
		break;
		}
	}
	
	private void opracovanieRolky(Sprava sprava)
	{
		Rolka rolka = sprava.rolka();
		Sklad sklad = myAgent().sklad(idSkladu(sprava));
		SimQueue< Sprava > front = myAgent().rolkyCakajuceNaOpracovanie(sklad.id());

		rolka.setSklad(sklad);

		if (rolka.sklad().id() == Id.sklad4)
		{
			sprava.setAddressee(myAgent().findAssistant(Id.procesPredexpedicnehoSpracovaniaRolky));
			startContinualAssistant(sprava);
		}
		else // opracovanie rolky
		{
			sprava.setAddressee(myAgent().findAssistant(Id.dotazVolnyTim));
			execute(sprava);

			if (sprava.timPracovnikov() != null)
			{
				sprava.timPracovnikov().setPracuje(true);
				sprava.timPracovnikov().setRolka(sprava.rolka());
				sprava.setAddressee(myAgent().findAssistant(Id.procesOpracovaniaRolky));
				startContinualAssistant(sprava);
			}
			else
			{
				front.enqueue(sprava);
			}
		}
	}
	
	private void pridelenieMiestaDokoncene(Sprava sprava)
	{
		Sklad sklad = myAgent().sklad(idSkladu(sprava));
		sklad.pridajRolku(sprava);
		sprava.setCode(Mc.pridelenieMiestaVSkladeDokoncene);
		response(sprava);
	}
	
	@Override
	public AgentSkladov myAgent()
	{ return (AgentSkladov)super.myAgent(); }
	
	private int idSkladu(Sprava sprava)
	{
		Rolka rolka = sprava.rolka();
		
		if (rolka.sklad() != null)
		{
			return rolka.sklad().id();
		}
		else
		{
			if (rolka.stanovisko() == Stanovisko.S1) return Id.sklad1;
			else if (rolka.stanovisko() == Stanovisko.S2) return Id.sklad2;
			else if (rolka.stanovisko() == Stanovisko.S3)
			{
				if (rolka.jeOpracovana())
				{
					return Id.sklad4;
				}
				else return Id.sklad3;
			}
			else { throw new SimException("Toto sa nemalo stat..."); }
		}		
	}	
}
