package supermarket.gui;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import supermarket.agenti.AgentOddeleniaSPracovnikom;
import supermarket.agenti.AgentPokladne;
import supermarket.entity.Zakaznik;

public class AgnetSPracovnikomTableModel implements TableModel
{
	private AgentOddeleniaSPracovnikom _agent;
	
	private final int _colCount = 8;
	private final int _idColIndex = 0;
	private final int _state = 1;
	private final int _queueColIndex = 2;
	private final int _timeInQueue = 3;
	private final int _totalTime = 4;
	private final int _itemCountColIndex = 5;
	private final int _vegetableCount = 6;
	private final int _meatCheeseCount = 7;

	public AgnetSPracovnikomTableModel (AgentOddeleniaSPracovnikom agent)
	{
		_agent = agent;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
//		switch (columnIndex)
//		{
//		case _idColIndex:
//		case _itemCountColIndex:
//		case _queueColIndex:
//			return Integer.class;
//		case _timeInQueue:
			return String.class;
//		}
//		return null;
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
			return "Zakazník (id)";
		case _itemCountColIndex:
			return "Počet položiek";
		case _queueColIndex:
			return "Číslo pracovníka";
		case _timeInQueue:
			return "Čas čakania";
		case _totalTime:
			return "Celkový čas čakania";
		case _meatCheeseCount:
			return "Počet položiek mäso/syr";
		case _vegetableCount:
			return "Počet položiek zelenina";
		case _state:
			return "Stav";
		}
		return "(null)";
	}

	@Override
	public int getRowCount()
	{
		return _agent.pocetZakaznikov();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Zakaznik zakaznik = null;
		try {
			zakaznik = _agent.zakaznik(rowIndex);
			
			if (zakaznik == null)
			{
				
				return null;
			}
		
		double t0;
		switch (columnIndex)
		{
		case _idColIndex:
			return zakaznik.id();
		case _itemCountColIndex:
			return zakaznik.pocetTovarov();
		case _queueColIndex:
			if (_agent.indexPracovnika(rowIndex) < 0)
			{
				System.out.println("-1");;
			}
			return _agent.indexPracovnika(rowIndex);
		case _timeInQueue:
			t0 = zakaznik.casZaradeniaDoFrontu();
			return Program.formatTime(t0 == -1 ? 0 : zakaznik.mySim().currentTime()-t0);
		case _totalTime:
			t0 = zakaznik.casZaradeniaDoFrontu();
			return Program.formatTime(zakaznik.celkovyCasCakania() + (t0 == -1 ? 0 : zakaznik.mySim().currentTime()-t0));
		case _meatCheeseCount:
			return zakaznik.pocetMasoASyr();
		case _vegetableCount:
			return zakaznik.pocetZelenina();
		case _state:
			if (zakaznik.casZaradeniaDoFrontu() == -1)
			{
				if (_agent instanceof AgentPokladne)
				{
					return "platí";
				}
				return "nakupuje";
			}
			return "čaká";
		}
		} catch (RuntimeException ex)
		{
			ex.printStackTrace();
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
