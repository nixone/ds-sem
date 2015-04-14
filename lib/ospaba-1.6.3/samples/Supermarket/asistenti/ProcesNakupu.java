package supermarket.asistenti;

import supermarket.entity.Zakaznik;
import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.TriangularRNG;

public class ProcesNakupu extends ContinualAssistant
{
	private static TriangularRNG _tria = new TriangularRNG(40d, 120d, 800d);
	
	public ProcesNakupu(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);		
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			
			message.setCode(Mc.procesNakupuOstatneHotovy);
			hold(casNakupu(((Sprava)message).zakaznik()), message); // naplanuje ukoncenie nakupu na cas simCas + casNakupu
		break;

		case Mc.procesNakupuOstatneHotovy:
			
			assistantFinished(message);
		break;
		}
	}
	
	private double casNakupu(Zakaznik zakaznik)
	{
		double casNakupu = .0;
		for (int i = 0; i < zakaznik.pocetOstatnychTovarov(); ++i)
		{
			casNakupu += _tria.sample();
		}
		return casNakupu;
	}
}