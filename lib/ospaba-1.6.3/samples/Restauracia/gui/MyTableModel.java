package gui;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import agenti.AgentJedalne;
import agenti.AgentKuchyne;

public abstract class MyTableModel implements TableModel
{
	protected AgentJedalne _agentJedalne;
	protected AgentKuchyne _agentKuchyne;

	public MyTableModel(AgentJedalne agentJedalne, AgentKuchyne agentKuchyne)
	{
		_agentJedalne = agentJedalne;
		_agentKuchyne = agentKuchyne;
	}


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
