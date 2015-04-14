package gui;

import java.util.List;

import riadiaciAgenti.AgentSkladov;
import simulacia.Id;
import entity.TimPracovnikov;
import OSPABA.Simulation;

public class TimyTableModel extends MyTableModel
{
	private final int id = 0;
	private final int pracuje = 1;
	private final int sklad = 2;
	private final int rolka = 3;
	private final int vytazenost = 4;
	
	private final int _colCount = 5;
	
	private List< TimPracovnikov > _timy;

	public TimyTableModel(Simulation sim)
	{
		super(sim);
		_timy = ((AgentSkladov)sim().findAgent(Id.agentSkladov)).timiPracovnikov();
	}

	@Override
	public int getRowCount()
	{
		return _timy.size();
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
		case id: return "Sys.id";
		case pracuje: return "Stav";
		case sklad: return "Sklad";
		case rolka: return "Rolka (Sys.id)";
		case vytazenost: return "Vyťaženosť";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		TimPracovnikov tim = _timy.get(rowIndex);
		
		switch (columnIndex)
		{
		case id: return tim.id();
		case pracuje: return tim.presuvaSa() ? "Presúva sa" : tim.pracuje() ? "Pracuje" : "Nepracuje";
		case sklad: return tim.sklad();
		case rolka: return tim.rolka() != null ? tim.rolka().id() : "-";
		case vytazenost: return String.format("%.1f", 100d * tim.casPrace() / tim.mySim().currentTime()) + "%";
		}
		return null;
	}
}
