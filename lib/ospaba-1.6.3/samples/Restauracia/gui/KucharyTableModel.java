package gui;

import agenti.AgentKuchyne;
import entity.Kuchar;

public class KucharyTableModel extends PracovniciTableModel
{	
	final int objednavka = 4;

	public KucharyTableModel(AgentKuchyne agentKuchyne)
	{
		super(null, agentKuchyne);
	}

	@Override
	public int getColumnCount()
	{
		return super.getColumnCount() + 1;
	}

	@Override
	public String getColumnName(int colIndex)
	{
		String ret = super.getColumnName(colIndex);
		if (ret != null) return ret;

		switch (colIndex)
		{
		case objednavka:
			return "Objednavka (Sys.id)";
		}
		return null;
	}

	@Override
	public int getRowCount()
	{
		return _agentKuchyne.kuchari().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		Kuchar kuchar = (Kuchar)_agentKuchyne.kuchari().get(rowIndex);
		Object ret = super.getValueFor(kuchar, colIndex);
		if (ret != null) return ret;

		switch (colIndex)
		{
		case objednavka:
			if (kuchar.objednavka() != null)
			{
				return kuchar.objednavka().id();
			}
			return "-";
		}
		return null;
	}
}
