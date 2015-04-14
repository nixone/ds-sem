package entity;

import simulacia.Id;
import simulacia.Sprava;
import simulacia.Stanovisko;
import OSPABA.Simulation;
import OSPExceptions.SimException;

public class Sklad extends KolekciaRoliek
{
	public Sklad(int id, Simulation mySim, int kapacita)
	{
		super(id, mySim, kapacita);		
	}

	public void opracujRolku(Sprava sprava)
	{
		sprava.rolka().setOpracovana(true);
	}

	public int pocetOpracovanychRoliek()
	{
		int pocet = 0;
		for (Sprava sprava : rolky())
		{
			if (id() == Id.sklad4 && sprava.rolka().jePripravenaNaExpedovanie())
				++pocet;
			else if (sprava.rolka().jeOpracovana())
				++pocet;
		}
		return pocet;
	}
	
	public int pocetNeopracovanychRoliek()
	{
		return pocetRoliek() - pocetOpracovanychRoliek();
	}
	
	public boolean jeVolneMiesto()
	{
		return pocetRoliek() < kapacita();
	}
	
	public Stanovisko stanovisko()
	{
		switch (id())
		{
		case Id.sklad1:
			return Stanovisko.S1;
		
		case Id.sklad2:
			return Stanovisko.S2;
		
		case Id.sklad3:
		case Id.sklad4:
			return Stanovisko.S3;
		}
		throw new SimException("neexistujuci sklad " + this);
	}
	
	public String cislo()
	{
		switch (id())
		{
		case Id.sklad1: return "S1";
		case Id.sklad2: return "S2";
		case Id.sklad3: return "S3";
		case Id.sklad4: return "S4";
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return cislo();
	}
}
