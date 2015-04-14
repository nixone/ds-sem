package supermarket.asistenti;

import supermarket.entity.Zakaznik;
import supermarket.simulacia.Mc;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.TriangularRNG;

public class ProcesPlatenia extends ContinualAssistant
{
	private static TriangularRNG _tria = new TriangularRNG(1d, 7d, 90d);		//	private static ExponentialRNG _tria = new ExponentialRNG(45); // 400 45

	public ProcesPlatenia(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:

			message.setCode(Mc.procesPlateniaHotovy);
			hold(casNakupu(((Sprava)message).zakaznik()), message);
		break;

		case Mc.procesPlateniaHotovy:

			assistantFinished(message);
		break;
		}
	}
	
	private double casNakupu(Zakaznik zakaznik)
	{	
		double casNakupu = 0;
		for (int i = 0; i < zakaznik.pocetTovarov(); ++i)
		{
			casNakupu += _tria.sample();
		}
		return casNakupu;
	}
}