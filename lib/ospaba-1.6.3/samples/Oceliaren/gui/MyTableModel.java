package gui;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import OSPABA.Simulation;

public abstract class MyTableModel implements TableModel
{
	private Simulation _sim;

	public MyTableModel(Simulation sim)
	{
		super();
		_sim = sim;
	}
	
	public Simulation sim()
	{ return _sim; }

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1)
	{ return false; }

	@Override
	public void addTableModelListener(TableModelListener arg0)
	{ }
	@Override
	public void removeTableModelListener(TableModelListener arg0)
	{ }
	@Override
	public void setValueAt(Object arg0, int arg1, int arg2)
	{ }
}
