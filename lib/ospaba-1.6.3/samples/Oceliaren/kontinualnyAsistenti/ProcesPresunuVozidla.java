package kontinualnyAsistenti;

import dynamickyAgenti.AgentVozidla;
import simulacia.Mc;
import simulacia.Sprava;
import simulacia.Stanovisko;
import OSPABA.CommonAgent;
import OSPABA.ContinualAssistant;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ProcesPresunuVozidla extends ContinualAssistant
{
	public ProcesPresunuVozidla(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		AgentVozidla vozidlo = sprava.vozidlo();
		Stanovisko ciel = sprava.ciel();
		
		switch (sprava.code())
		{
		case Mc.start:
			double vzdialenost = vozidlo.stanovisko().vzdialenost(ciel); // vzdialenost v metroch
			double rychlost = vozidlo.rychlost(); // rychlost v metroch za minutu
			vozidlo.setStoji(false);
			vozidlo.setCiel(ciel);
			
			sprava.setCode(Mc.hold);
			hold(vzdialenost/rychlost, sprava);
		break;
		
		case Mc.hold:
			vozidlo.setStanovisko(ciel);
			vozidlo.setStoji(true);
			assistantFinished(message);
		break;
		}
	}
}
