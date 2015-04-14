package promptnyAsistenti;

import OSPABA.Adviser;
import entity.TimPracovnikov;
import riadiaciAgenti.AgentSkladov;
import simulacia.Id;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.UniformContinuousRNG;

public class PravidloPrenunuTimov extends Adviser
{
	private static UniformContinuousRNG _unif = new UniformContinuousRNG(0d, 1d);
	private int [] _idSkladov;

	public PravidloPrenunuTimov(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		_idSkladov = new int [] { Id.sklad1, Id.sklad2, Id.sklad3, Id.sklad4 };
	}

	@Override
	public void execute(MessageForm message) // tim sa presunie do najzaplnenejsieho skladu s 10% pravdepodobnostou
	{
		Sprava sprava = (Sprava)message;
		TimPracovnikov tim = sprava.timPracovnikov();
		
		boolean presun = false;
		
		double [] zaplnenie =
		{
			myAgent().sklad(Id.sklad1).zaplnenie(),
			myAgent().sklad(Id.sklad2).zaplnenie(),
			myAgent().sklad(Id.sklad3).zaplnenie()
		};
		double [] zaplnenieNeopracovane =
			{
				myAgent().sklad(Id.sklad1).pocetNeopracovanychRoliek() / (double)myAgent().sklad(Id.sklad1).kapacita(),
				myAgent().sklad(Id.sklad2).pocetNeopracovanychRoliek() / (double)myAgent().sklad(Id.sklad2).kapacita(),
				myAgent().sklad(Id.sklad3).pocetNeopracovanychRoliek() / (double)myAgent().sklad(Id.sklad3).kapacita(),
			};
			
		int index = argmax(zaplnenie);
		
		if (tim.sklad().pocetNeopracovanychRoliek() == 0) // ak uz nie je v sklade co robyt, presunie sa do dalsieho
		{
			presun = true;
		}
		else if (.8 < zaplnenie[index] && tim.sklad().zaplnenie() < .7)
		{
			presun = true;
		}
		else if (_unif.sample() < .3)
		{	
			if (_idSkladov[index] != tim.sklad().id())
			{
				presun = true;
			}
		}
		
		if (presun)
		{
			if (zaplnenieNeopracovane[index] < .1) // ak je vybrany sklad zaplneny opracovanymi rolkami
			{
				index = argmax(zaplnenieNeopracovane);
			}
			sprava.setIdSkladu(_idSkladov[index]);
			sprava.setMsgResult(1);
		}
		else
		{
			sprava.setMsgResult(0);
		}
	}
	
	private int argmax(double [] arr)
	{
		double max = arr[0];
		int index = 0;
		
		for (int i = 1; i < arr.length; ++i)
		{
			if (max < arr[i])
			{
				max = arr[i];
				index = i;
			}
		}
		return index;
	}
	
//	private int argmax(double [] arr, int ignorovanyIndex)
//	{
//		double max = arr[0];
//		int index = 0;
//		
//		for (int i = 1; i < arr.length; ++i)
//		{
//			if (i != ignorovanyIndex && max < arr[i])
//			{
//				max = arr[i];
//				index = i;
//			}
//		}
//		return index;
//	}
	
	@Override
	public AgentSkladov myAgent()
	{ return (AgentSkladov)super.myAgent(); }
}
