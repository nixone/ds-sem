package riadiaciAgenti;

import riadiaciManazeri.ManazerModelu;
import simulacia.Id;
import simulacia.Mc;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentModelu extends Agent
{
	public AgentModelu(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		new ManazerModelu(Id.manazerModleu, mySim, this);
		
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.prichodRolky);
		addOwnMessage(Mc.spracovanieRolkyDokoncene);
	}
	
	public void inicializaciaSimulacie()
	{
		Sprava sprava = new Sprava(mySim());
		sprava.setCode(Mc.init);
		sprava.setAddressee(this);
		manager().notice(sprava);
	}
}
