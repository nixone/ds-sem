package gui;

import agenti.AgentJedalne;
import entity.Casnik;

public class CasnciTableModel extends PracovniciTableModel
{
	private final int _stav = 4;
	
	public CasnciTableModel(AgentJedalne agentJedalne)
	{
		super(agentJedalne, null);
	}

	@Override
	public int getColumnCount()
	{
		return super.getColumnCount() + 1;
	}
	
	@Override
	public int getRowCount()
	{
		return _agentJedalne.casnici().size();
	}

	@Override
	public String getColumnName(int colIndex)
	{
		String ret = super.getColumnName(colIndex);
		if (ret != null) return ret;

		switch (colIndex)
		{
		case _stav:
			return "Stav";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		Casnik casnik = (Casnik)_agentJedalne.casnici().get(rowIndex);
		Object ret = super.getValueFor(casnik, colIndex);
		if (ret != null) return ret;

		switch (colIndex)
		{
		case _stav:
			return casnik.stav();
		}
		return null;
	}
}
