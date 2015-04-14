package supermarket.asistenti;

import supermarket.entity.Zakaznik;
import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.EmpiricRNG;
import OSPRNG.EmpiricPair;
import OSPRNG.UniformDiscreteRNG;

public class ProcesNakupuMasaASyru extends ContinualAssistant
{
	private static EmpiricRNG _empiric = new EmpiricRNG(
			new EmpiricPair(new UniformDiscreteRNG(2, 10), .1),
			new EmpiricPair(new UniformDiscreteRNG(11, 20), .2),
			new EmpiricPair(new UniformDiscreteRNG(21, 34), .3),
			new EmpiricPair(new UniformDiscreteRNG(35, 41), .25),
			new EmpiricPair(new UniformDiscreteRNG(42, 50), .1),
			new EmpiricPair(new UniformDiscreteRNG(51, 100), .5)
		);

	public ProcesNakupuMasaASyru(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:

			message.setCode(Mc.procesNakupuMasaSyruHotovy);
			hold(casNakupu(((Sprava)message).zakaznik()), message);
		break;

		case Mc.procesNakupuMasaSyruHotovy:		
			
			assistantFinished(message);	
		break;
		}
	}
	
	double casNakupu(Zakaznik zakaznik)
	{
		double casNakupu = 0;
		for (int i = 0; i < zakaznik.pocetMasoASyr(); ++i)
		{
			casNakupu += _empiric.sample().intValue();
		}
		return casNakupu;
	}
}
