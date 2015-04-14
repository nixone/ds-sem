package gui;

import agenti.AgentKuchyne;
import entity.Objednavka;

public class ObjednavkaTableModel extends MyTableModel
{
	final int id = 0;
	final int pocetPokrmov = 2;
	final int pocetPripravene = 4;
	final int pocetNepripravene = 5;
	final int pocetPripravovane = 3;
	final int zakaznik = 1;
	final int cisloStola = 6;

	public ObjednavkaTableModel(AgentKuchyne agentKuchyne)
	{
		super(null, agentKuchyne);
	}

	@Override
	public int getColumnCount()
	{
		return 5;
	}

	@Override
	public String getColumnName(int colIndex)
	{
		switch (colIndex)
		{
		case id:
			return "Sys.id";
		case pocetPokrmov:
			return "Počet pokrmov";
		case pocetPripravene:
			return "Počet pripravených pokrmov";
		case pocetPripravovane:
			return "Počet pripravovaných pokrmov";
		case pocetNepripravene:
			return "Počet nepripravených pokrmov";
		case zakaznik:
			return "Zakaznici (Sys.id)";
		case cisloStola:
			return "Číslo stola";
		}
		return null;
	}

	@Override
	public int getRowCount()
	{
		return _agentKuchyne.objednavky().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		Objednavka objednavka = _agentKuchyne.objednavky().get(rowIndex);
		switch (colIndex)
		{
		case id:
			return objednavka.id();
		case pocetPokrmov:
			return objednavka.pokrmy().size();
		case pocetPripravene:
			return objednavka.pocetPripravenychPokrmov();
		case pocetPripravovane:
			return objednavka.pocetPripravovanychPokrmov();
		case pocetNepripravene:
			return objednavka.pocetNepripravenychPokrmov();
		case zakaznik:
			return objednavka.objednavajuci().id();
		case cisloStola:
			return objednavka.objednavajuci().stol().cislo();
		}
		return null;
	}
}
