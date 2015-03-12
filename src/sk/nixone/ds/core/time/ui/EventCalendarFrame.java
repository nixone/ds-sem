package sk.nixone.ds.core.time.ui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.SimulationRun;
import sk.nixone.ds.core.time.SimulationRun.Observer;

public class EventCalendarFrame extends JFrame implements Observer {
	
	private JTable table;
	
	private JScrollPane scrollPane;
	
	private AbstractTableModel model = new AbstractTableModel() {
		
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
	};
	
	private List<PlannedEvent> eventCalendar = null;
	
	public EventCalendarFrame() {
		super("Event calendar");
		
		createLayoutAndComponents();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void createLayoutAndComponents() {
		table = new JTable(model);
		scrollPane = JTable.createScrollPaneForTable(table);
		
		setContentPane(scrollPane);
		scrollPane.setPreferredSize(new Dimension(600, 400));
		pack();
	}

	@Override
	public void onEventPlanned(SimulationRun run, PlannedEvent event) {
		eventCalendar = run.getPlannedEvents();
		model.fireTableDataChanged();
	}

	@Override
	public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent) {
		eventCalendar = run.getPlannedEvents();
		model.fireTableDataChanged();
		
		setTitle("Event calendar "+String.format("%.1f", run.getCurrentSimulationTime()));
	}

	@Override
	public void onVoidStep(SimulationRun run) {
		setTitle("Event calendar "+String.format("%.1f", run.getCurrentSimulationTime()));
	}
}
