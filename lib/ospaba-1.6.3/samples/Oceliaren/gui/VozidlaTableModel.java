package gui;

import java.util.List;

import riadiaciAgenti.AgentOceliarne;
import riadiaciAgenti.AgentVozidiel;
import riadiaciAgenti.AgentZeriavov;
import simulacia.Id;
import dynamickyAgenti.AgentVozidla;
import OSPABA.Simulation;
import OSPExceptions.SimException;

public class VozidlaTableModel extends MyTableModel
{
	private List< AgentVozidla > _vozidla;
	
	private final int cislo = 0;
	private final int stanovisko = 1;
	private final int rychlost = 2;
	private final int rolka = 3;
	private final int stav = 4;
	private final int vytazenie = 5;
	private final int riadiaciAgent = 6;
	
	private final int _colCount = 7;

	public VozidlaTableModel(Simulation sim)
	{
		super(sim);	
		_vozidla = ((AgentVozidiel)sim.findAgent(Id.agentVozidiel)).vozidla();
	}

	@Override
	public int getRowCount()
	{
		return _vozidla.size();
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
		case stanovisko: return "Stanovisko";
		case rychlost: return "Rýchlosť";
		case rolka: return "Rolka";
		case stav: return "Stav";
		case vytazenie: return "Vyťaženie";
		case riadiaciAgent: return "Riadiaci agent";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		AgentVozidla vozidlo = _vozidla.get(rowIndex);

		switch (columnIndex)
		{
		case cislo: return vozidlo.cislo();
		case stanovisko: return vozidlo.stanovisko() + (vozidlo.stoji() ? "" : "->" + vozidlo.ciel());
		case rychlost: return (vozidlo.stoji() ? 0 : String.format("%.1f", vozidlo.rychlost() * 60 / 1000)) + " km/h";
		case rolka: return vozidlo.rolka() != null ? vozidlo.rolka().id() : "-";
		case stav: return vozidlo.stoji() ? "stojí" : vozidlo.rolka() == null ? "presúva sa" : "vezie rolku";
		case vytazenie: return String.format("%.1f", 100d * vozidlo.casPrace() / (0.0000001 + vozidlo.mySim().currentTime())) + "%";
		case riadiaciAgent:
			if (vozidlo.parent() instanceof AgentZeriavov) return "Zeriavov";
			else if (vozidlo.parent() instanceof AgentVozidiel) return "Vozidiel";
			else if (vozidlo.parent() instanceof AgentOceliarne) return "Oceliarne";
			else throw new SimException("Invalid state");
		}
		return null;
	}

}
