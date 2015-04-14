package promptnyAsistenti;

import riadiaciAgenti.AgentZeriavov;
import simulacia.Sprava;
import entity.Rolka;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Query;
import OSPABA.Simulation;

public class DotazVyberZeriavu extends Query
{
	public DotazVyberZeriavu(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		
//		String kam = "";
		int idZeriavu;
		Rolka rolka = ((Sprava)message).rolka();

		// rolka moze byt v sklade xor vo vozidle xor na dopravniku
		assert(1 == (rolka.sklad() == null ? 0:1) + (rolka.vozidlo() == null ? 0:1) + (rolka.dopravnik() == null ? 0:1));
		
		idZeriavu = rolka.stanovisko().idZeriavu();
		
//		if (rolka.sklad() != null) // ak je rolka v sklade
//		{
//			idZeriavu = rolka.sklad().stanovisko().idZeriavu();
//			kam = "sklad " + rolka.sklad().cislo();
//		}
//		else if (rolka.vozidlo() != null) // rolka je vo vozidle
//		{
//			idZeriavu = rolka.vozidlo().stanovisko().idZeriavu();
//			kam = "vozidlo " + rolka.vozidlo().cislo();
//		}
//		else if (rolka.dopravnik() != null) // rolka je na dopravniku
//		{
//			idZeriavu = rolka.dopravnik().stanovisko().idZeriavu();
//			kam = "dopravnik " + rolka.dopravnik().cislo();
//		}
		sprava.setIdZeriavu(idZeriavu);
	}

	@Override
	public AgentZeriavov myAgent()
	{ return (AgentZeriavov)super.myAgent(); }
}
