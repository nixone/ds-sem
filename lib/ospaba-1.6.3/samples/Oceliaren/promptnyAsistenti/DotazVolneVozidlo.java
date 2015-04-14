package promptnyAsistenti;

import java.util.LinkedList;
import java.util.List;

import riadiaciAgenti.AgentVozidiel;
import simulacia.Sprava;
import dynamickyAgenti.AgentVozidla;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Query;
import OSPABA.Simulation;

public class DotazVolneVozidlo extends Query
{
	public DotazVolneVozidlo(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		sprava.setVozidlo(null);
		
		List< AgentVozidla > volneVozidla = new LinkedList<>();
	
		// najde volne vozidla
		for (AgentVozidla vozidlo : myAgent().vozidla())
		{
			if (vozidlo.jeVolne())
			{
				volneVozidla.add(vozidlo);
			}
		}
		
		double minVzdialenost = Double.MAX_VALUE;

		// vyberie volne vozidlo ktore je najblizsie k rolke
		for (AgentVozidla vozidlo : volneVozidla)
		{
			double vzdialenost = vozidlo.stanovisko().vzdialenost(sprava.rolka().stanovisko());
			
			if (vzdialenost < minVzdialenost)
			{
				minVzdialenost = vzdialenost;
				sprava.setVozidlo(vozidlo);
			}
		}
	}

	@Override
	public AgentVozidiel myAgent()
	{ return (AgentVozidiel)super.myAgent(); }
}
