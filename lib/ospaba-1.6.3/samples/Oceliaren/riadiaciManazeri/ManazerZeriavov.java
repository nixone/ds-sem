package riadiaciManazeri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dynamickyAgenti.AgentVozidla;
import entity.Dopravnik;
import entity.Zeriav;
import riadiaciAgenti.AgentDopravnikov;
import riadiaciAgenti.AgentZeriavov;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPExceptions.SimException;

public class ManazerZeriavov extends Manager
{
	private Map<AgentVozidla, Sprava> _vozidla;

	public ManazerZeriavov(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		_vozidla = new HashMap<>();
	}

	@Override
	public void processMessage(MessageForm message)
	{
 		Sprava sprava = (Sprava)message;
 		Zeriav zeriav;

		switch (sprava.code())
		{
		case Mc.pridelenieZeriavu:
			myAgent().dotazVyberZeriavu().execute(sprava); // vyberie zeriav ktory rolku prelozi
			myAgent().pridajRolkuDoFrontuCakajucichNaPridelenieZeriavu(sprava);
			
			if (! myAgent().zeriav(sprava.idZeriavu()).pracuje())
			{
				pridelenieZeriavu(sprava);
			}
		break;

		case Mc.prelozenieRolky:
			sprava.setAddressee(myAgent().procesPrelozeniaRolky());
			startContinualAssistant(sprava);
		break;
		
		case Mc.finish: // ProcesPrelozeniaRolky
			zeriav = myAgent().zeriav(sprava.idZeriavu());
			if (zeriav.peekPristaveneVozidlo() != null)
			{
				Sprava copy = _vozidla.remove(zeriav.popPristaveneVozidlo());
				returnDynamicAgent(copy);
			}
			
			sprava.setCode(Mc.prelozenieRolkyDokoncene);
			response(sprava);
		break;
		
		case Mc.uvolnenieZeriavu:
			zeriav = myAgent().zeriav(sprava.idZeriavu());
			zeriav.setPracuje(false);
			zeriav.setRolka(null);
			
			if (0 < myAgent().pocetCakajucichNaPridelenieZeriavu(sprava.idZeriavu()))
			{
				pridelenieZeriavu(sprava);
			}

		break;
		
		case Mc.entrustDA:
			myAgent().addDynamicAgent(sprava.vozidlo());
			myAgent().zeriav(sprava.vozidlo().stanovisko()).pushPristaveneVozidlo(sprava.vozidlo());
			_vozidla.put(sprava.vozidlo(), sprava);
		break;
		}
	}

	@Override
	public AgentZeriavov myAgent()
	{ return (AgentZeriavov)super.myAgent(); }
	
	static ArrayList<Sprava> spracovane = new ArrayList<>();
	
	private void pridelenieZeriavu(Sprava sprava)
	{
		Zeriav zeriav = myAgent().zeriav(sprava.idZeriavu());

		if (sprava.idZeriavu() == Id.zeriav3)
		{
			sprava = new Sprava(sprava);
			sprava.setAddressee(myAgent().pravidloVystupnychRoliek()); // vyberie rolku ktora ma byt prelozena
			execute(sprava);
			
			Dopravnik d3 = ((AgentDopravnikov)mySim().findAgent(Id.agentDopravnikov)).dopravnik(Id.dopravnik3);
			if (sprava.rolka().vozidlo() == null
				&& d3.kapacita() <= 1 + d3.pocetRoliek() + (zeriav.pracuje() ? 1 : 0))
			{
				myAgent().pridajRolkuDoFrontuCakajucichNaPridelenieZeriavu(sprava);
				return;
			}
			
			if (sprava.msgResult() == -1)
			{ throw new SimException("Toto sa nemalo stat..."); }
		}
		else
		{
			sprava = myAgent().cakajuceNaZ12(sprava.idZeriavu()).dequeue();
		}
		
		if (zeriav.pracuje())
		{
			myAgent().pridajRolkuDoFrontuCakajucichNaPridelenieZeriavu(sprava); // frontCakajucichNaPridelenieZeriavu(sprava.idZeriavu()).enqueue(sprava);
		}
		else
		{
			zeriav.setRolka(sprava.rolka());
			zeriav.setPracuje(true);
			
			sprava.setCode(Mc.pridelenieZeriavuDokoncene);
			response(sprava);
		}
	}
}
