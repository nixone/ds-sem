package asistenti;

import java.util.LinkedList;
import java.util.List;

import entity.SkupinaZakaznikov;
import simulacia.Mc;
import simulacia.SimulaciaRestovracie;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Process;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.RNGTemplate;
import OSPRNG.TriangularRNG;

public class ProcesJedenia extends Process
{
	private static RNGTemplate _rngTemplate = new RNGTemplate(TriangularRNG.class, 60 * 3d, 60 * 15d, 60 * 30d);

	private TriangularRNG _tria;
	private List< SkupinaZakaznikov > _zakaznici;

	public ProcesJedenia(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		_zakaznici = new LinkedList<>();
		_tria = (TriangularRNG)_rngTemplate.generator(((SimulaciaRestovracie)mySim).id());
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		switch (sprava.code())
		{
		case Mc.start:
			_zakaznici.add(sprava.zakaznici());
			message.setCode(Mc.jedenieUkoncene);			
			hold(casJedenia(sprava.zakaznici()), message);	
		break;

		case Mc.jedenieUkoncene:
			_zakaznici.remove(sprava.zakaznici());
			assistantFinished(message);
		break;
		}
	}
	
	public List< SkupinaZakaznikov >zakaznici()
	{ return _zakaznici; }

	private double casJedenia(SkupinaZakaznikov zakaznici)
	{
		double maxCasJedenia = _tria.sample();

		for (int i = 1; i < zakaznici.pocet(); ++i)
		{
			double casJedenia = _tria.sample();
			if (maxCasJedenia < casJedenia)
			{
				maxCasJedenia = casJedenia;
			}
		}
		return maxCasJedenia;
	}
}
