package sk.nixone.ds.core.time.ui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.Simulation;
import sk.nixone.ds.core.time.Simulation.Observer;
import sk.nixone.ds.core.time.SimulationRun;

public class EventCalendarPanel extends JPanel implements Emitter<Object> {

	private JTable table;
	
	private JScrollPane scrollPane;
	
	private List<PlannedEvent> eventCalendar = null;
	
	private EventCalendarTableModel model = new EventCalendarTableModel();
	
	private Simulation simulation;
	
	public EventCalendarPanel(Simulation simulation) {
		this.simulation = simulation;
		
		table = new JTable(model);
		scrollPane = JTable.createScrollPaneForTable(table);
		scrollPane.setPreferredSize(new Dimension(600, 100));
		
		add(scrollPane);
	}
	
	@Override
	public void reset() {
		eventCalendar = null;
		model.fireTableDataChanged();
	}

	@Override
	public void emit(Object o) {
		eventCalendar = simulation.getCurrentSimulationRun().getPlannedEvents();
		model.fireTableDataChanged();
	}
	
	public class EventCalendarTableModel extends AbstractTableModel {

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(eventCalendar == null) {
				return null;
			}
			
			PlannedEvent event = eventCalendar.get(rowIndex);
			if(event == null) {
				return null;
			}
			
			switch(columnIndex) {
			case 0: return String.format("%.1f", event.getExecutionTime());
			case 1: return event.getEvent().toString();
			}
			return null;
		}
		
		@Override
		public int getRowCount() {
			if(eventCalendar == null) {
				return 0;
			}
			return eventCalendar.size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex) {
			case 0: return "Execution time";
			case 1: return "Event";
			}
			return null;
		}
		
		@Override
		public int getColumnCount() {
			return 2;
		}
	}
}
