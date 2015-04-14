package asistenti;

import OSPABA.Action;
import java.util.List;

import entity.SkupinaZakaznikov;
import entity.Stol;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class AkciaPriradStol extends Action
{
	private List< Stol > _stoli;

	public AkciaPriradStol(int id, Simulation mySim, Agent myAgent, List< Stol > stoli)
	{
		super(id, mySim, myAgent);
		_stoli = stoli;
	}

	@Override
	public void execute(MessageForm message)
	{
		SkupinaZakaznikov zakaznici = ((Sprava)message).zakaznici();
		zakaznici.setStol(najdiVhodnyStol(zakaznici));
	}

	private Stol najdiVhodnyStol(SkupinaZakaznikov zakaznici)
	{
		Stol vybranyStol = null;
		for (Stol stol : _stoli)
		{
			if (jeStolVhodnejsi(vybranyStol, stol, zakaznici))
			{
				vybranyStol = stol;
			}
		}
		return vybranyStol;
	}
	
	private boolean jeStolVhodnejsi(Stol povodnyStol, Stol novyStol, SkupinaZakaznikov zakaznici)
	{
		if (novyStol.jeObsadeny()
			|| novyStol.pocetMiest() < zakaznici.pocet())
		{
			return false;
		}
		return povodnyStol == null
			|| (novyStol.pocetMiest() < povodnyStol.pocetMiest() 
			&& zakaznici.pocet() <= novyStol.pocetMiest());
	}
}
