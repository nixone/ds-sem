package supermarket.gui;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import supermarket.entity.Zakaznik;

public class ZoznamZakaznikovTableModel implements TableModel
{
	private List<Zakaznik> _zakaznici;
	
	private final int _colCount = 4;
	private final int _idColIndex = 0;
	private final int _itemCountColIndex = 1;
	private final int _leaveTime = 2;
	private final int _totalWaitTime = 3;

	public ZoznamZakaznikovTableModel(List<Zakaznik> zakaznici)
	{
		_zakaznici = zakaznici;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
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
		case _idColIndex:
			return "Zákazník (id)";
		case _itemCountColIndex:
			return "Počet položiek";
		case _leaveTime:
			return "Čas";
		case _totalWaitTime:
			return "Doba čakania";
		}
		return "(null)";
	}

	@Override
	public int getRowCount()
	{
		return _zakaznici.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Zakaznik zakaznik = _zakaznici.get(rowIndex);
			
		if (zakaznik == null)
		{
			return null;
		}
		
		switch (columnIndex)
		{
		case _idColIndex:
			return zakaznik.id();
		case _itemCountColIndex:
			return zakaznik.pocetTovarov();
		case _leaveTime:
			return Program.formatTime(zakaznik.casCasUkonceniaObsluhy() + 7 * 3600);
		case _totalWaitTime:
			return Program.formatTime(zakaznik.celkovyCasCakania());
//			return zakaznik.celkovyCasCakania();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{ return false; }
	@Override
	public void addTableModelListener(TableModelListener l)
	{ ; }
	@Override
	public void removeTableModelListener(TableModelListener l)
	{ ; }
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{ ; }
}
