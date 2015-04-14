package kontinualnyAsistenti;

import simulacia.Id;
import entity.Rolka;
import OSPABA.CommonAgent;
import OSPABA.Simulation;
import OSPRNG.ExponentialRNG;

public class PlanovacPrichodovRoliek_S2 extends PlanovacPrichodovRoliek
{
	private static ExponentialRNG _exp = new ExponentialRNG(39.1);

	public PlanovacPrichodovRoliek_S2(int id, Simulation mySim, CommonAgent myAgent)
	{ super(id, mySim, myAgent); }

	@Override
	public double exp()
	{
		return 2 + _exp.sample();
	}
	
	@Override
	public Rolka novaRolka()
	{
		return new Rolka(mySim(), Id.sklad2);
	}
}