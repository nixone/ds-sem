package asistenti;

import agenti.AgentOkolia;
import entity.SkupinaZakaznikov;
import simulacia.Mc;
import simulacia.SimulaciaRestovracie;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Scheduler;
import OSPABA.Simulation;
import OSPRNG.ExponentialRNG;
import OSPRNG.RNGTemplate;

public class PlanovacPrichoduZakaznikov extends Scheduler
{
	private static RNGTemplate [] _rngTemplates = new RNGTemplate[AgentOkolia.kPocetSkupin];
	
	private ExponentialRNG _exp;
	private int _pocetZakaznikovVSkupine;
	private boolean _generatorAktyvny;
	
	static
	{
		double [] intenzityPrichodov = { 3600/3d, 3600/6d, 3600/6d, 3600/5d, 3600/2d, 3600/2d }; 
		for (int i = 0; i < AgentOkolia.kPocetSkupin; ++i)
		{
			_rngTemplates[i] = new RNGTemplate(ExponentialRNG.class, intenzityPrichodov[i]);
		}
	}

	public PlanovacPrichoduZakaznikov(int id, Simulation mySim, Agent myAgent, int pocetZakaznikovVSkupine)
	{
		super(id, mySim, myAgent);
		_pocetZakaznikovVSkupine = pocetZakaznikovVSkupine;
		_generatorAktyvny = true;
		
		_exp = (ExponentialRNG)_rngTemplates[_pocetZakaznikovVSkupine - 1].generator(((SimulaciaRestovracie)mySim).id());
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		switch (message.code())
		{
		case Mc.start:
			sprava.setCode(Mc.novyZakaznik);
			hold(_exp.sample(), sprava);
		break;

		case Mc.novyZakaznik:
			if (_generatorAktyvny)
			{
				Sprava msgCopy = sprava.createCopy();
				hold(_exp.sample(), msgCopy);

				sprava.setZakaznici(new SkupinaZakaznikov(mySim(), _pocetZakaznikovVSkupine));
	
				assistantFinished(sprava);
			}			
		break;
		
		case Mc.breakCA:
			_generatorAktyvny = false;	
		break;
		}
	}
}
