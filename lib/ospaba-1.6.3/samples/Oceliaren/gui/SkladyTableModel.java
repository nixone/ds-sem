package gui;

import java.util.ArrayList;
import java.util.List;

import entity.Sklad;
import riadiaciAgenti.AgentSkladov;
import simulacia.Id;
import OSPABA.Simulation;

public class SkladyTableModel extends MyTableModel
{
	private final int nazov = 0;
	private final int kapacita = 1;
	private final int zaplneny = 2;
	private final int opracovane = 3;
	private final int neopracovane = 4;
	
	private List< Sklad > _sklady;

	public SkladyTableModel(Simulation sim)
	{
		super(sim);	
		_sklady = new ArrayList<>(((AgentSkladov)sim().findAgent(Id.agentSkladov)).sklady().values());
	}
	
	@Override
	public int getRowCount()
	{
		return 4;
	}

	@Override
	public int getColumnCount()
	{
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch (columnIndex)
		{
		case nazov: return "Číslo";
		case kapacita: return "Kapacita";
		case zaplneny: return "Zaplnený";
		case opracovane: return "Opracované rolky";
		case neopracovane: return "Neopracované rolky";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Sklad sklad = _sklady.get(rowIndex);
		switch (columnIndex)
		{
		case nazov: return sklad.cislo();
		case kapacita: return (int)sklad.kapacita();
		case zaplneny: return String.format("%.1f", 100d * sklad.pocetRoliek() / sklad.kapacita())  + "%";
		case opracovane: return sklad.pocetOpracovanychRoliek();
		case neopracovane: return sklad.pocetNeopracovanychRoliek();
		}
		return null;
	}

}
