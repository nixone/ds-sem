package sk.nixone.ds.core.time.ui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.Simulation;
import sk.nixone.ds.core.time.Simulation.Observer;
import sk.nixone.ds.core.time.SimulationRun;

public class EventCalendarPanel extends JPanel implements Observer {

	private JTable table;
	
	private JScrollPane scrollPane;
	
	private List<PlannedEvent> eventCalendar = null;
	
	private EventCalendarTableModel model = new EventCalendarTableModel();
	
	public EventCalendarPanel(Simulation simulation) {		
		table = new JTable(model);
		scrollPane = JTable.createScrollPaneForTable(table);
		scrollPane.setPreferredSize(new Dimension(600, 100));
		add(scrollPane);
		
		simulation.addObserver(this);
	}

	@Override
	public void onSimulationStarted() {}

	@Override
	public void onReplicationStarted(int replicationIndex, SimulationRun run) {}

	@Override
	public void onEventPlanned(SimulationRun run, PlannedEvent event) {
		eventCalendar = run.getPlannedEvents();
		model.fireTableDataChanged();
	}

	@Override
	public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent) {
		eventCalendar = run.getPlannedEvents();
		model.fireTableDataChanged();
	}

	@Override
	public void onVoidStep(SimulationRun run) {}

	@Override
	public void onReplicationEnded(int replicationIndex, SimulationRun run) {
		eventCalendar = null;
		model.fireTableDataChanged();
	}

	@Override
	public void onSimulationEnded() {}
	
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
