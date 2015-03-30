package sk.nixone.ds.sem2;

import javax.swing.JFrame;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.ui.SimulationFrame;
import sk.nixone.ds.core.ui.StatisticPanel;
import sk.nixone.ds.core.ui.TimeStatisticPanel;
import sk.nixone.util.AppearanceUtil;

public class Application {
	public static void main(String[] args) {
		AppearanceUtil.setNiceSwingLookAndFeel();
		
		final Simulation simulation = new Simulation(new Randoms());
		
		SimulationFrame frame = new SimulationFrame(simulation);
		frame.addTab("Display & settings", new SimulationPanel(simulation));
		
		CapacityCorelationPanel corelationPanel = new CapacityCorelationPanel(simulation);
		simulation.getEnded().add(corelationPanel);
		frame.addTab("Corelation", corelationPanel);
		
		StatisticPanel panel = new StatisticPanel(simulation, simulation.servedPeople, "Served people");
		simulation.getReplicationEnded().add(panel);
		frame.addTab("Served count", panel);
		
		panel = new TimeStatisticPanel(simulation, simulation.globalStayInSystem, "Global stay in system");
		simulation.getReplicationEnded().add(panel);
		frame.addTab("T. in system", panel);
		
		panel = new StatisticPanel(simulation, simulation.globalQueueLength, "Global queue length");
		simulation.getReplicationEnded().add(panel);
		frame.addTab("Q length", panel);
		
		panel = new StatisticPanel(simulation, simulation.globalDetectorQueueLength, "Global detector queue length");
		simulation.getReplicationEnded().add(panel);
		frame.addTab("Detector Q length", panel);
		
		panel = new StatisticPanel(simulation, simulation.localQueueLength, "Local queue length");
		simulation.getSimulationUpdated().add(panel);
		frame.addTab("Q length (R)", panel);
		
		panel = new StatisticPanel(simulation, simulation.localDetectorQueueLength, "Local detector queue length");
		simulation.getSimulationUpdated().add(panel);
		frame.addTab("Detector Q length (R)", panel);
		
		panel = new TimeStatisticPanel(simulation, simulation.localStayInSystem, "Local stay in system");
		simulation.getSimulationUpdated().add(panel);
		frame.addTab("T. in system (R)", panel);
		
		frame.setTitle("Simulation of airport security check (C) Martin Olešnaník");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
