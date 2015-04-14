package riadiaciAgenti;

import kontinualnyAsistenti.PlanovacPrichodovRoliek;
import kontinualnyAsistenti.PlanovacPrichodovRoliek_S1;
import kontinualnyAsistenti.PlanovacPrichodovRoliek_S2;
import kontinualnyAsistenti.PlanovacPrichodovRoliek_S3;
import riadiaciManazeri.ManazerOkolia;
import simulacia.Id;
import simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentOkolia extends Agent
{	
	private PlanovacPrichodovRoliek [] _planovacePrichodovRoliek;

	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		new ManazerOkolia(Id.manazerOkolia, mySim, this);
		
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.odchodRolky);
		
		addOwnMessage(Mc.hold);
		
		_planovacePrichodovRoliek = new PlanovacPrichodovRoliek[3];
		
		_planovacePrichodovRoliek[0] = new PlanovacPrichodovRoliek_S1(Id.planovacPrichoduRoliek_S1, mySim, this);
		_planovacePrichodovRoliek[1] = new PlanovacPrichodovRoliek_S2(Id.planovacPrichoduRoliek_S2, mySim, this);
		_planovacePrichodovRoliek[2] = new PlanovacPrichodovRoliek_S3(Id.planovacPrichoduRoliek_S3, mySim, this);
	}
	
	public PlanovacPrichodovRoliek [] planovacePrichodovRoliek()
	{ return _planovacePrichodovRoliek; }
}
