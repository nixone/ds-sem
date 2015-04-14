package gui;

import java.util.ArrayList;
import java.util.List;

import riadiaciAgenti.AgentDopravnikov;
import simulacia.Id;
import entity.Dopravnik;
import OSPABA.Simulation;

public class DopravnikyTableModel extends MyTableModel
{
	private List< Dopravnik > _dopravniky;
	
	private final int cislo = 0;
	private final int typ = 1;
	private final int kapacita = 2;
	private final int pocetRoliek = 3;
	private final int zaplneny = 4;
	
	private final int _colCount = 5;

	
	public DopravnikyTableModel(Simulation sim)
	{
		super(sim);
		_dopravniky = new ArrayList<>(((AgentDopravnikov)sim.findAgent(Id.agentDopravnikov)).dopravniky().values());
	}

	@Override
	public int getRowCount()
	{
		return 3;
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
		case typ: return "Typ";
		case kapacita: return "Kapacita";
		case pocetRoliek: return "Počet roliek";
		case zaplneny: return "Zaplnený";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Dopravnik dopravnik = _dopravniky.get(rowIndex);
		switch (columnIndex)
		{
		case cislo: return dopravnik.cislo();
		case typ: return dopravnik.jeVstupnyDopravnik() ? "Vstupný" : "Výstupný";
		case kapacita: return dopravnik.kapacita();
		case pocetRoliek: return dopravnik.pocetRoliek();
		case zaplneny: return String.format("%.1f", dopravnik.zaplnenie() * 100) + "%";
		}
		return null;
	}
}
