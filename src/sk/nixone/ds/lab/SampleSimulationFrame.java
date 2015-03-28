package sk.nixone.ds.lab;

import sk.nixone.ds.core.time.ui.SimulationFrame;
import sk.nixone.ds.core.ui.StatisticPanel;

public class SampleSimulationFrame extends SimulationFrame {

	public SampleSimulationFrame(SampleSimulation simulation) {
		super(simulation);
		
		StatisticPanel panel = new StatisticPanel(simulation, simulation.getCustomerInSystemTime(), "Time customer spent in system");
		simulation.getSimulationUpdated().add(panel);
		addTab("T. in system", panel);
		
		panel = new StatisticPanel(simulation, simulation.getCustomerProcessTime(), "Process time");
		simulation.getSimulationUpdated().add(panel);
		addTab("Process t.", panel);
		
		panel = new StatisticPanel(simulation, simulation.getCustomerWaitingTime(), "Waiting time for start");
		simulation.getSimulationUpdated().add(panel);
		addTab("Wait t.", panel);
	}
	
}
