package gui;

import agenti.AgentJedalne;
import entity.Stol;

public class StolTableModel extends MyTableModel
{
	final int cislo = 0;
	final int pocetMiset = 1;
	final int jeObsadeny = 2;
	final int idZakaznici = 3;
	
	public StolTableModel(AgentJedalne agentJedalne)
	{
		super(agentJedalne, null);
	}

	@Override
	public int getColumnCount()
	{
		return 4;
	}

	@Override
	public String getColumnName(int colIndex)
	{
		switch (colIndex)
		{
		case cislo:
			return "Číslo stola";
		case pocetMiset:
			return "Počet miest";
		case jeObsadeny:
			return "Je obsadený";
		case idZakaznici:
			return "Zakaznici (Sys.id)";
		}
		return null;
	}

	@Override
	public int getRowCount()
	{
		return _agentJedalne.stoli().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		Stol stol = _agentJedalne.stoli().get(rowIndex);
		
		switch (colIndex)
		{
		case cislo:
			return stol.cislo();
		case pocetMiset:
			return stol.pocetMiest() + " miestny";
		case jeObsadeny:
			return stol.jeObsadeny() ? "Obsadený" : "Voľný";
		case idZakaznici:
			return stol.zakaznici() == null ? "-" : stol.zakaznici().id();
		}
		return null;
	}

}
