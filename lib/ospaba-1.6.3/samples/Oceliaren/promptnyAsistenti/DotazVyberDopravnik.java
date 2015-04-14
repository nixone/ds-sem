package promptnyAsistenti;

import entity.Rolka;
import simulacia.Id;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Query;
import OSPABA.Simulation;
import OSPRNG.UniformContinuousRNG;

public class DotazVyberDopravnik extends Query
{
	private static UniformContinuousRNG _uniform = new UniformContinuousRNG(0d, 1d);

	public DotazVyberDopravnik(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		Rolka rolka = ((Sprava)message).rolka();
		
		if (rolka.jePripravenaNaExpedovanie())
		{
			message.setMsgResult(Id.dopravnik3);
		}
		else
		{
			if (rolka.cielovySklad() == Id.sklad1)
			{
				message.setMsgResult(Id.dopravnik1);
			}
			else if (rolka.cielovySklad() == Id.sklad2)
			{
				message.setMsgResult(Id.dopravnik2);
			}
			else if (rolka.cielovySklad() == Id.sklad3)
			{
				if (rolka.jeOpracovana())
				{
					message.setMsgResult(Id.dopravnik3);
				}
				else
				{
					double p = _uniform.sample();
					
					if (p < .7)
					{
						message.setMsgResult(Id.dopravnik2);
					}
					else // if (p >= .7)
					{
						message.setMsgResult(Id.dopravnik1);
					}
				}
			}
		}
	}
}
