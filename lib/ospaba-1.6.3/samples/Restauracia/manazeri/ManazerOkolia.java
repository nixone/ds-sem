package manazeri;

import agenti.AgentOkolia;
import asistenti.PlanovacPrichoduZakaznikov;
import entity.SkupinaZakaznikov;
import entity.Zakaznik;
import simulacia.Config;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerOkolia extends Manager
{	
	private int _pocetObsluzenychZakaznikov;
	private int _pocetNeobsluzenychZakaznikov;
	private int _pocetVygenerovanychZakaznikov;
	
	public ManazerOkolia(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		_pocetObsluzenychZakaznikov = 0;
		_pocetNeobsluzenychZakaznikov = 0;
		_pocetVygenerovanychZakaznikov = 0;
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		SkupinaZakaznikov zakaznici = sprava.zakaznici();

		switch (message.code())
		{
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.planovacPrichoduZakaznikov:
				
				if (mySim().currentTime() < Config.casOtvoreniaRestauracie)
				{
					if (Config.casOdKtorehoZakazniciCakajuPredRestauraciou < mySim().currentTime())
					{
						myAgent().cakajuciPredOtvorenim().add(zakaznici);
					}
					break;
				}
				if (Config.casZatvoreniaRestauracie < mySim().currentTime())
				{
					for (PlanovacPrichoduZakaznikov planovac : myAgent().planovacePrichodov())
					{
						Sprava messageCopy = sprava.createCopy();
						messageCopy.setAddressee(planovac);
						breakContinualAssistant(messageCopy);
					}
					break;
				}
				
				message.setCode(Mc.prichodZakaznika);
				message.setAddressee(mySim().findAgent(Id.agentModelu));
				notice(message);
				
				_pocetVygenerovanychZakaznikov += zakaznici.pocet();
			break;
		
			case Id.planovacOtvoreniaRestauracie:
				for (SkupinaZakaznikov skupinaZakaznikov : myAgent().cakajuciPredOtvorenim())
				{
					Sprava messageCopy = sprava.createCopy();
					messageCopy.setZakaznici(skupinaZakaznikov);
					messageCopy.setCode(Mc.novyZakaznik);
					messageCopy.setAddressee(this);
					
					notice(messageCopy);
				}
				myAgent().cakajuciPredOtvorenim().clear();
			break;
			}
		break;

		case Mc.odchodZakaznika:
			if (mySim().currentTime() < Config.casZatvoreniaRestauracie)
			{
				if (zakaznici.odchadzajuNeobsluzeny())
				{
					_pocetNeobsluzenychZakaznikov += zakaznici.pocet();
				}
				else
				{
					_pocetObsluzenychZakaznikov += zakaznici.pocet();
	
					for (Zakaznik zakaznik : zakaznici.zakaznici())
					{
						myAgent().statCelkovyCasCakania().addSample(zakaznik.casCakania());
					}
				}
			}
		break;
		}	
	}

	public int pocetObsluzenychZakaznikov()
	{ return _pocetObsluzenychZakaznikov; }
	
	public int pocetNeobsluzenychZakaznikov()
	{ return _pocetNeobsluzenychZakaznikov; }

	public int pocetVygenerovanychZakaznikov()
	{ return _pocetVygenerovanychZakaznikov; }
	
	@Override
	public AgentOkolia myAgent()
	{ return (AgentOkolia)super.myAgent(); }
}
