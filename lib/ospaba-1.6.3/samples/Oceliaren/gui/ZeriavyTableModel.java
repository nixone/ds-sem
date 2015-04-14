package gui;

import java.util.ArrayList;
import java.util.List;

import riadiaciAgenti.AgentZeriavov;
import simulacia.Id;
import entity.Zeriav;
import OSPABA.Simulation;

public class ZeriavyTableModel extends MyTableModel
{
	private List< Zeriav > _zeriavy;
	
	private final int cislo = 0;
	private final int stav = 1;
	private final int rolka = 2;
	private final int vytazenost = 3;
	
	private final int _colCount = 4;

	public ZeriavyTableModel(Simulation sim)
	{
		super(sim);
		_zeriavy = new ArrayList<>(((AgentZeriavov)sim.findAgent(Id.agentZeriavov)).zeriavy().values());
	}

	@Override
	public int getRowCount()
	{
		return _zeriavy.size();
	}

	@Override
	public int getColumnCount()
	{
		return _colCount;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch (columnIndex)
		{
		case cislo: return "Číslo";
		case stav: return "Stav";
		case rolka: return "Rolka";
		case vytazenost: return "Vyťaženosť";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Zeriav zeriav = _zeriavy.get(rowIndex);

		switch (columnIndex)
		{
		case cislo: return zeriav.cislo();
		case stav:
			if (zeriav.pracuje())
			{
				return "Prekladá";
			}
			else return "Nepracuje";
		case rolka: return zeriav.rolka() != null ? zeriav.rolka().id() : "-";
		case vytazenost: return String.format("%.1f", 100d * zeriav.casPrace() / zeriav.mySim().currentTime()) + "%";
		}
		return null;
	}
}
