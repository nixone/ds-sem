package manazeri;

import agenti.AgentJedalne;
import entity.Casnik;
import entity.SkupinaZakaznikov;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerJedalne extends Manager
{
	public ManazerJedalne(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		SkupinaZakaznikov zakaznici = sprava.zakaznici();
		Casnik casnik = null;
		
		switch (sprava.code())
		{
		case Mc.obsluhaZakaznika:
			myAgent().akciaPriradStol().execute(sprava);
			
			if (zakaznici.stol() != null)
			{				
				myAgent().cakajuciNaObjednanieJedla().enqueue(zakaznici);
				zakaznici.zaciatokCakania();
				zakaznici.setStav("Čaká na objednanie");

				priradenieCasnika(sprava);
			}
			else // nie je volny stol
			{				
				zakaznici.setOdchadzajuNeobsluzeny(true);
				zakaznici.setStav("Odchádza neobslužený");;
				
				sprava.setCode(Mc.obsluhaZakaznikaUkoncena);				
				response(sprava);
			}
		break;

		case Mc.pripravaJedlaUkoncena:
			myAgent().cakajuciNaPrinesenieJedla().enqueue(zakaznici);
			priradenieCasnika(sprava);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.procesObjednavania:
				
				zakaznici.setStav("Čaká na prinesenie jedla");
				
				casnik = (Casnik)sprava.pracovnik();

				uvolnenieCasnika(casnik);
				priradenieCasnika(sprava);
				
				// pripravenie jedla v kuchini
				sprava.setCode(Mc.pripravaJedla);
				sprava.setAddressee(mySim().findAgent(Id.agentRestovracie));
				zakaznici.zaciatokCakania();
				
				request(sprava);
			break;
	
			case Id.procesPrineseniaJedla:
				casnik = (Casnik)sprava.pracovnik();
				
				uvolnenieCasnika(casnik);
				priradenieCasnika(sprava);
				
				// zaciatok jedenia prineseneho jedla
				zakaznici.setStav("Je jedlo");
				zakaznici.koniecCakania();

				sprava.setAddressee(myAgent().procesJedenia());
				startContinualAssistant(sprava);
			break;
			
			case Id.procesJedenia:
				zakaznici.setStav("Čaká na platenie");
	
				myAgent().cakajuciNaPlatenie().enqueue(zakaznici);
				zakaznici.zaciatokCakania();
	
				priradenieCasnika(sprava);
			break;

			case Id.procesPlatenia:
				casnik = (Casnik)sprava.pracovnik();
	
				uvolnenieCasnika(casnik);
				priradenieCasnika(sprava);
				
				zakaznici.uvolniStol();
				zakaznici.setStav("Odchádza");
	
				// odchod
				sprava.setCode(Mc.obsluhaZakaznikaUkoncena);			
				response(sprava);
			break;
			}
		break;
		}
	}

	private void priradenieCasnika(Sprava sprava)
	{
		Sprava msgCopy = sprava.createCopy();

		myAgent().dotazNajkratsiePracujuciCasnik().execute(msgCopy);
		Casnik casnik = (Casnik)msgCopy.pracovnik();	
				
		if (casnik == null) return; // vsetci casnici pracuju
				
		if (0 < myAgent().cakajuciNaObjednanieJedla().size())
		{
			objednanie(msgCopy);
			casnik.setStav("Preberá objednavku");
		}
		else if (0 < myAgent().cakajuciNaPrinesenieJedla().size())
		{
			prinesenie(msgCopy);
			casnik.setStav("Nesie jedlo");
		}
		else if (0 < myAgent().cakajuciNaPlatenie().size())
		{
			platenie(msgCopy);
			casnik.setStav("Preberá platbu");
		}
		else return; // casnik nema koho obsluzit

		casnik.zacniPracu(msgCopy.zakaznici());
		aktualizujStatPocetVolnychCasnikov();

		startContinualAssistant(msgCopy);
	}
	
	private void uvolnenieCasnika(Casnik casnik)
	{
		casnik.skonciPracu();
		aktualizujStatPocetVolnychCasnikov();
	}
	
	private void objednanie(Sprava sprava)
	{
		sprava.setZakaznici(myAgent().cakajuciNaObjednanieJedla().pop());
		sprava.setAddressee(myAgent().procesObjednavania());
		sprava.zakaznici().setStav("Objednáva");
		sprava.zakaznici().koniecCakania();
	}
	
	private void prinesenie(Sprava sprava)
	{
		sprava.setZakaznici(myAgent().cakajuciNaPrinesenieJedla().pop());
		sprava.setAddressee(myAgent().procesPrineseniaJedla());
	}
	
	private void platenie(Sprava sprava)
	{
		sprava.setZakaznici(myAgent().cakajuciNaPlatenie().pop());
		sprava.setAddressee(myAgent().procesPlatenia());
		sprava.zakaznici().setStav("Platí");
		sprava.zakaznici().koniecCakania();
	}
	
	private void aktualizujStatPocetVolnychCasnikov()
	{
		if (myAgent().pocetVolnychCasnikovStat() != null)
		{
			myAgent().pocetVolnychCasnikovStat().addSample(myAgent().pocetVolnychCasnikov());
		}
	}
	
	@Override
	public AgentJedalne myAgent()
	{ return (AgentJedalne)super.myAgent(); }
}
