package manazeri;

import agenti.AgentKuchyne;
import entity.Kuchar;
import entity.Objednavka;
import simulacia.Config;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerKuchyne extends Manager
{
	public ManazerKuchyne(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		
		switch (sprava.code())
		{
		case Mc.pripravaJedla:
			myAgent().frontObjednavok().push(sprava.objednavka());
			zacatiePripravyJedla(sprava);
		break;

		case Mc.finish: // procesPripravyJedla
			Kuchar kuchar = (Kuchar)sprava.pracovnik();
			Objednavka objednavka = sprava.objednavka();
			
			kuchar.skonciPracu();
			myAgent().pocetVolnychKucharovStat().addSample(myAgent().pocetVolnychKucharov());
			objednavka.pokrmPripraveny();
			
			if (aktualnaObjednavka() != null)
			{
				zacatiePripravyJedla(sprava);
			}
			if (objednavka.jeVsetkoPripravene())
			{
				sprava.setZakaznici(objednavka.objednavajuci());
				sprava.setCode(Mc.pripravaJedlaUkoncena);
				response(sprava);
				
				if (Config.casZatvoreniaRestauracie < mySim().currentTime()
					&& myAgent().objednavky().isEmpty())
				{
					myAgent().pocetVolnychKucharovStat().addSample(myAgent().pocetVolnychKucharov());
				}
			}
		break;
		}
	}
	
	private void zacatiePripravyJedla(Sprava sprava)
	{
		Objednavka objednavka;

		while ((objednavka = aktualnaObjednavka()) != null)
		{
			myAgent().dotazNajkratsiePracujuciKuchar().execute(sprava);
			Kuchar kuchar = (Kuchar)sprava.pracovnik();
			
			if (kuchar != null)
			{				
				kuchar.zacniPracu(objednavka);
				myAgent().pocetVolnychKucharovStat().addSample(myAgent().pocetVolnychKucharov());

				Sprava msgCopy = sprava.createCopy();
				msgCopy.setObjednavka(objednavka);
				msgCopy.setPokrm(objednavka.dalsiPokrm());
				msgCopy.setAddressee(myAgent().procesPripravyJedla());
				
				startContinualAssistant(msgCopy);
			}
			else { break; } // vsetci kuchari uz pracuju
		}
	}
	
	private Objednavka aktualnaObjednavka()
	{
		Objednavka objednavka = null;
		if (0 < myAgent().frontObjednavok().size())
		{ 
			objednavka = myAgent().frontObjednavok().peek();
			if (!objednavka.maDalsiPokrm())
			{
				myAgent().frontObjednavok().dequeue();
				objednavka = aktualnaObjednavka();
			}
		} 
		return objednavka;
	}

	@Override
	public AgentKuchyne myAgent()
	{ return (AgentKuchyne)super.myAgent(); }
}

