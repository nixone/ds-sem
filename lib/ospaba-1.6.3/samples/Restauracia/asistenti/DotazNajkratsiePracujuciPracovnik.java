package asistenti;

import java.util.List;

import entity.Pracovnik;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Query;
import OSPABA.Simulation;

public class DotazNajkratsiePracujuciPracovnik extends Query
{
	private List< Pracovnik > _pracovnici;

	public DotazNajkratsiePracujuciPracovnik(int id, Simulation mySim, Agent myAgent, List< Pracovnik > pracovnici)
	{
		super(id, mySim, myAgent);
		_pracovnici = pracovnici;
	}

	@Override
	public void execute(MessageForm message)
	{
		double minCas = Double.MAX_VALUE;
		Pracovnik najkratsiePracujuciPracovnik = null;

		for (Pracovnik pracovnik : _pracovnici)
		{
			if (pracovnik.pracuje()) continue;
			if (pracovnik.casPrace() < minCas)
			{
				najkratsiePracujuciPracovnik = pracovnik;
				minCas = pracovnik.casPrace();
			}
		}
		((Sprava)message).setPracovnik(najkratsiePracujuciPracovnik);
	}
}
