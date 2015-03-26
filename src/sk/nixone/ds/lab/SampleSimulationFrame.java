package sk.nixone.ds.lab;

import sk.nixone.ds.core.time.ObserverEmitterAdapter;
import sk.nixone.ds.core.time.ui.SimulationFrame;
import sk.nixone.ds.core.ui.StatisticPanel;

public class SampleSimulationFrame extends SimulationFrame {

	public SampleSimulationFrame(SampleSimulation simulation) {
		super(simulation);
		
		ObserverEmitterAdapter adapter = new ObserverEmitterAdapter();
		simulation.addObserver(adapter);
		
		StatisticPanel panel = new StatisticPanel(simulation, simulation.getCustomerInSystemTime(), "Time customer spent in system");
		adapter.addSimulationUpdaterEmitter(panel);
		addTab("T. in system", panel);
		
		panel = new StatisticPanel(simulation, simulation.getCustomerProcessTime(), "Process time");
		adapter.addSimulationUpdaterEmitter(panel);
		addTab("Process t.", panel);
		
		panel = new StatisticPanel(simulation, simulation.getCustomerWaitingTime(), "Waiting time for start");
		adapter.addSimulationUpdaterEmitter(panel);
		addTab("Wait t.", panel);
	}
	
}
