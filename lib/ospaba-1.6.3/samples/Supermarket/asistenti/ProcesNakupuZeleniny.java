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

public class ProcesNakupuZeleniny extends ContinualAssistant
{
	private static EmpiricRNG _empiric = new EmpiricRNG(
			new EmpiricPair(new UniformDiscreteRNG(2, 5), .3),
			new EmpiricPair(new UniformDiscreteRNG(6, 10), .5),
			new EmpiricPair(new UniformDiscreteRNG(11, 21), .1),
			new EmpiricPair(new UniformDiscreteRNG(22, 25), .09),
			new EmpiricPair(new UniformDiscreteRNG(26, 100), .01)
		);

	public ProcesNakupuZeleniny(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);	
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:

			message.setCode(Mc.procesNakupuZeleninyHotovy);
			hold(casNakupu(((Sprava)message).zakaznik()), message);
		break;

		case Mc.procesNakupuZeleninyHotovy:

			assistantFinished(message);
		break;
		}
	}
	
	private double casNakupu(Zakaznik zakaznik)
	{
		double casNakupu = 0;
		for (int i = 0; i < zakaznik.pocetZelenina(); ++i)
		{
			casNakupu += _empiric.sample().intValue();
		}
		return casNakupu;
	}
}