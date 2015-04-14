package asistenti;

import simulacia.Mc;
import simulacia.SimulaciaRestovracie;
import OSPABA.Agent;
import OSPABA.Process;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.RNGTemplate;
import OSPRNG.UniformContinuousRNG;

public class ProcesPrineseniaJedla extends Process
{
	private static RNGTemplate _rngTemplate = new RNGTemplate(UniformContinuousRNG.class, 30d, 110d);
	
	private UniformContinuousRNG _uniform;

	public ProcesPrineseniaJedla(int id, Simulation mySim, Agent myAgent) 
	{
		super(id, mySim, myAgent);
		_uniform = (UniformContinuousRNG)_rngTemplate.generator(((SimulaciaRestovracie)mySim).id());
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			message.setCode(Mc.prinesenieUkoncene);			
			hold(_uniform.sample(), message);	
		break;

		case Mc.prinesenieUkoncene:
			assistantFinished(message);
		break;
		}
	}

}
