package asistenti;

import entity.Objednavka;
import simulacia.Mc;
import simulacia.SimulaciaRestovracie;
import simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Process;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.RNGTemplate;
import OSPRNG.UniformContinuousRNG;

public class ProcesObjednavania extends Process
{
	private static RNGTemplate _rngTemplate = new RNGTemplate(UniformContinuousRNG.class, 55d, 145d);

	private UniformContinuousRNG _uniform;

	public ProcesObjednavania(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		_uniform = (UniformContinuousRNG)_rngTemplate.generator(((SimulaciaRestovracie)mySim).id());
	}

	@Override
	public void processMessage(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		switch (sprava.code())
		{
		case Mc.start:
			sprava.setCode(Mc.objednanieUkoncene);
			hold(_uniform.sample(), sprava);	
		break;

		case Mc.objednanieUkoncene:
			sprava.setObjednavka(new Objednavka(mySim(), sprava.zakaznici()));
			assistantFinished(sprava);
		break;
		}
	}
}
