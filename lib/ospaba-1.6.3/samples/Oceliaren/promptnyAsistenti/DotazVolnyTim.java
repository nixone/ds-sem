package promptnyAsistenti;

import riadiaciAgenti.AgentSkladov;
import simulacia.Sprava;
import entity.TimPracovnikov;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Query;
import OSPABA.Simulation;

public class DotazVolnyTim extends Query
{
	public DotazVolnyTim(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		sprava.setTimPracovnikov(null);

		for (TimPracovnikov tim : myAgent().timiPracovnikov())
		{
			if (! tim.pracuje() && ! tim.presuvaSa() && tim.sklad().stanovisko() == sprava.rolka().stanovisko())
			{
				sprava.setTimPracovnikov(tim);
			}
		}
	}

	@Override
	public AgentSkladov myAgent()
	{ return (AgentSkladov)super.myAgent(); }
}
