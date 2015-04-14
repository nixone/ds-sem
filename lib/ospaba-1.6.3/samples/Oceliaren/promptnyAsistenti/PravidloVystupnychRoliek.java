package promptnyAsistenti;

import OSPABA.Adviser;
import entity.Sklad;
import riadiaciAgenti.AgentSkladov;
import riadiaciAgenti.AgentZeriavov;
import simulacia.Id;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;

public class PravidloVystupnychRoliek extends Adviser
{

	public PravidloVystupnychRoliek(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		Sprava vybranaSprava = null;

		if (! myAgent().cakajuceNaVozidlo().isEmpty()) // rolky cakajuce vo vozidlach maju prednost
		{
			vybranaSprava = myAgent().cakajuceNaVozidlo().dequeue();
		}
		else
		{
				// rolka je vlozena zo skladu ktori je menej zaplneny [%]
				AgentSkladov agentSkladov = ((AgentSkladov)mySim().findAgent(Id.agentSkladov));
	
				Sklad sklad3 = agentSkladov.sklad(Id.sklad3);
				Sklad sklad4 = agentSkladov.sklad(Id.sklad4);
	
				SimQueue< Sprava > vybranyFront = sklad3.zaplnenie() < sklad4.zaplnenie() ? myAgent().cakajuceS4() : myAgent().cakajuceS3();
				SimQueue< Sprava > zaloznyFront = vybranyFront == myAgent().cakajuceS4() ? myAgent().cakajuceS3() : myAgent().cakajuceS4();
				
				if (0 < vybranyFront.size())
				{
					vybranaSprava = vybranyFront.dequeue();
				}
				else if (0 < zaloznyFront.size())
				{
					vybranaSprava = zaloznyFront.dequeue();
				}
		}
		if (vybranaSprava != null)
		{
			sprava.copy(vybranaSprava);
			sprava.setMsgResult(1);
		}
		else sprava.setMsgResult(-1);
	}
	
	@Override
	public AgentZeriavov myAgent()
	{ return (AgentZeriavov)super.myAgent(); }
}
