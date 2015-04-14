package supermarket.gui;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import supermarket.agenti.AgentOddeleniaSPracovnikom;
import supermarket.entity.Pracovnik;

public class PracovniciTableModel implements TableModel
{
	private AgentOddeleniaSPracovnikom _agent;
	
	private final int _pocetStlpcov = 5;
	private final int _cisloPracovnika = 0;
	private final int _stav = 1;
	private final int _dlzkaFrontu = 2;
	private final int _obsluhovanyZakaznik = 3;
	private final int _pocetObsluzenychZakaznikov = 4;

	public PracovniciTableModel (AgentOddeleniaSPracovnikom agent)
	{
		_agent = agent;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}

	@Override
	public int getColumnCount()
	{
		return _pocetStlpcov;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch (columnIndex)
		{
		case _cisloPracovnika:
			return "Číslo pracovníka";
		case _stav:
			return "Stav";
		case _dlzkaFrontu:
			return "Dĺžka frontu";
		case _obsluhovanyZakaznik:
			return "Obsluhovaný zákazník (id)";
		case _pocetObsluzenychZakaznikov:
			return "Počet obslúžených zákazníkov";
		}
		return "(null)";
	}

	@Override
	public int getRowCount()
	{
		return _agent.pracovnici().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Pracovnik pracovnik = _agent.pracovnici().get(rowIndex);

		switch (columnIndex)
		{
		case _cisloPracovnika:
			return rowIndex+1;

		case _stav:
			if (pracovnik.obsluhovanyZakaznik() == null)
				return "čaká";
			return "pracuje";
	
		case _dlzkaFrontu:
			return pracovnik.dlzkaFrontu();
	
		case _obsluhovanyZakaznik:
			if (pracovnik.obsluhovanyZakaznik() != null)
			{
				return pracovnik.obsluhovanyZakaznik().id();
			}
			return "-";
			
		case _pocetObsluzenychZakaznikov:
			return pracovnik.pocetObsluzenychZakaznikov();
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
