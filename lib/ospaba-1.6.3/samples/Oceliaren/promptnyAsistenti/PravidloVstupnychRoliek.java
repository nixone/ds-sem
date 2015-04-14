package promptnyAsistenti;

import OSPABA.Adviser;
import java.util.List;

import riadiaciAgenti.AgentDopravnikov;
import riadiaciAgenti.AgentSkladov;
import simulacia.Id;
import simulacia.Sprava;
import OSPABA.CommonAgent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPRNG.UniformContinuousRNG;

public class PravidloVstupnychRoliek extends Adviser
{
	private List< Sprava > _cakajuci_S4;
	private List< Sprava > _cakajuci_S3;
	
	private double [] _zaplnenie;
	
	private UniformContinuousRNG _unif = new UniformContinuousRNG(0d, 1d);

	public PravidloVstupnychRoliek(int id, Simulation mySim, CommonAgent myAgent, List< Sprava > cakajuciS4, List< Sprava > cakajuciS3)
	{
		super(id, mySim, myAgent);
		
		_cakajuci_S4 = cakajuciS4;
		_cakajuci_S3 = cakajuciS3;
	}

	@Override
	public void execute(MessageForm message)
	{
		Sprava sprava = (Sprava)message;
		Sprava vybranaSprava = null;
		
		AgentDopravnikov agentDopravnikov = (AgentDopravnikov)(mySim().findAgent(Id.agentDopravnikov));
		AgentSkladov agentSkladov = (AgentSkladov)(mySim().findAgent(Id.agentSkladov));
		
		_zaplnenie = new double [] {
				agentDopravnikov.dopravnik(Id.dopravnik1).zaplnenie(),
				agentDopravnikov.dopravnik(Id.dopravnik2).zaplnenie(),
				agentSkladov.sklad(Id.sklad1).zaplnenie(),
				agentSkladov.sklad(Id.sklad2).zaplnenie()
			};

//		int argmax = argmax(_zaplnenie);
		
		double p = _unif.sample();
				
		if (0 < _cakajuci_S3.size() && p < .9 || _cakajuci_S4.isEmpty()) // cakajuce na prevoz maju prednost v 90% pripadoch
		{
			int dopravnik = _unif.sample() < .7 ? Id.dopravnik2 : Id.dopravnik1;
			
			for (int i = 0; i < _cakajuci_S3.size(); ++i)
			{
				if (_cakajuci_S3.get(i).rolka().dopravnik().id() == dopravnik)
				{
					if (maBytRolkaPrevezena(vybranaSprava, dopravnik))
					{
						vybranaSprava = _cakajuci_S3.remove(i);
					}
					break;
				}
			}
			if (vybranaSprava == null)
			{
				if (maBytRolkaPrevezena(vybranaSprava, dopravnik == Id.dopravnik1 ? Id.dopravnik2 : Id.dopravnik1))
				{
					vybranaSprava = _cakajuci_S3.remove(0);
				}
			}
		}
		
		if (vybranaSprava == null && ! _cakajuci_S4.isEmpty()) // presun zo skladov do S4
		{
			int zaplnenySklad = _zaplnenie[2] < _zaplnenie[3] ? Id.sklad2 : Id.sklad1; // z menej zaplneneho skladu
			for (int i = 0; i < _cakajuci_S4.size(); ++i)
			{
				if (_cakajuci_S4.get(i).rolka().sklad().id() == zaplnenySklad)
				{
					if (maBytRolkaPrevezena(vybranaSprava, zaplnenySklad))
					{
						vybranaSprava = _cakajuci_S4.remove(i);
					}
					break;
				}
			}
			if (vybranaSprava == null)// && 0 < _cakajuci_S4.size())
			{
				if (maBytRolkaPrevezena(vybranaSprava, zaplnenySklad == Id.sklad1 ? Id.sklad2 : Id.sklad1))
				{
					vybranaSprava = _cakajuci_S4.remove(0);
				}
			}
		}
		sprava.copy(vybranaSprava);
		
//		if (vybranaSprava != null)
//		{
//			sprava.copy(vybranaSprava);
//			sprava.setMsgResult(1);
//		}
//		else
//		{
//			sprava.setMsgResult(0);
//		}
	}
	
//	private int argmax(double [] arr)
//	{
//		double max = 0;
//		int index = -1;
//		
//		for (int i = 0; i < arr.length; ++i)
//		{
//			if (max < arr[i])
//			{
//				max = arr[i];
//				index = i;
//			}
//		}
//		return index;
//	}
	
	private boolean maBytRolkaPrevezena(Sprava rolka, int id) // tato strategia nie je pouzita
	{
		return true;
		
//		AgentSkladov agentSkladov = (AgentSkladov)(mySim().findAgent(Id.agentSkladov));
//
//		double [] zaplnenieOut = {
//				agentSkladov.sklad(Id.sklad3).zaplnenie(),
//				agentSkladov.sklad(Id.sklad4).zaplnenie()
//			};
//		
//		if (id == Id.dopravnik1 || id == Id.dopravnik2)
//		{
//			return true;
////			if (zaplnenieOut[0] < .9) return true;
//		}
//		if (id == Id.sklad1 || id == Id.sklad2)
//		{
//			if (zaplnenieOut[1] < .85) return true;
//		}
//		return false;
	}

}



//Sprava sprava = (Sprava)message;
//Sprava vybranaSprava = null;
//
//AgentDopravnikov agentDopravnikov = (AgentDopravnikov)(mySim().findAgent(Id.agentDopravnikov));
//AgentSkladov agentSkladov = (AgentSkladov)(mySim().findAgent(Id.agentSkladov));
//_zaplnenie = new double [] {
//		agentDopravnikov.dopravnik(Id.dopravnik1).zaplnenie(),
//		agentDopravnikov.dopravnik(Id.dopravnik2).zaplnenie(),
//		agentSkladov.sklad(Id.sklad1).zaplnenie(),
//		agentSkladov.sklad(Id.sklad2).zaplnenie()
//	};
//
//int argmax = argmax(_zaplnenie);
//
//if (argmax < 2 // dopravniky su viac zaplnene ako sklady
//	&& _cakajuci_S3.size() > _cakajuci_S4.size()) // dovodom zaplnenia su rolky cakajuce na presun do S3
//{
//	int zaplnenyDopravnikId = _zaplnenie[0] < _zaplnenie[1] ? Id.dopravnik2 : Id.dopravnik1;
//	for (int i = 0; i < _cakajuci_S3.size(); ++i)
//	{
//		if (_cakajuci_S3.get(i).rolka().dopravnik().id() == zaplnenyDopravnikId)
//		{
//			vybranaSprava = _cakajuci_S3.remove(i);
//			break;
//		}
//	}
//	if (vybranaSprava == null)
//	{
//		vybranaSprava = _cakajuci_S3.remove(0);
//	}
//}
//else // sklady su viac zaplnene ako dopravniky
//{
//	int zaplnenySklad = _zaplnenie[2] < _zaplnenie[3] ? Id.sklad2 : Id.sklad1;
//	for (int i = 0; i < _cakajuci_S4.size(); ++i)
//	{
//		if (_cakajuci_S4.get(i).rolka().sklad().id() == zaplnenySklad)
//		{
//			vybranaSprava = _cakajuci_S4.remove(i);
//			break;
//		}
//	}
//	if (vybranaSprava == null)
//	{
//		vybranaSprava = _cakajuci_S4.remove(0);
//	}
//}
//
//sprava.copy(vybranaSprava);
