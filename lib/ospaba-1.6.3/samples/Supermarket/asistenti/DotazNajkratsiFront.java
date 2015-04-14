package supermarket.asistenti;

import java.util.List;

import supermarket.entity.Pracovnik;
import supermarket.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.InstantAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class DotazNajkratsiFront extends InstantAssistant
{
	List< Pracovnik > _pracovnici;
	public DotazNajkratsiFront(int id, Simulation mySim, Agent myAgent, List< Pracovnik > pracovnici)
	{
		super(id, mySim, myAgent);
		_pracovnici = pracovnici;
	}

	@Override
	public void execute(MessageForm message)
	{
		int min = Integer.MAX_VALUE;
		int argMin = -1; // index pracovnika
		
		for (int i = 0; i < _pracovnici.size(); ++i)
		{
			if (_pracovnici.get(i).pocetZakaznikov() < min)
			{
				min = _pracovnici.get(i).pocetZakaznikov();
				argMin = i;
			}
		}
		if (argMin == -1)
		{
			return;
		}
		
		// index najkratsieho frontu je v argMin
		((Sprava)message).setPracovnik(_pracovnici.get(argMin));
	}
}
