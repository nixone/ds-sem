package sk.nixone.ds.sem2;

import javax.swing.JFrame;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.ui.SimulationFrame;
import sk.nixone.ds.core.ui.StatisticPanel;
import sk.nixone.util.AppearanceUtil;

public class Application {
	public static void main(String[] args) {
		AppearanceUtil.setNiceSwingLookAndFeel();
		
		final Simulation simulation = new Simulation(new Randoms(1));
		
		SimulationFrame frame = new SimulationFrame(simulation);
		frame.addTab("Display", new SimulationPanel(simulation));
		
		StatisticPanel panel = new StatisticPanel(simulation, simulation.servedPeople, "Served people");
		simulation.getReplicationEnded().add(panel);
		frame.addTab("Served", panel);
		
		panel = new StatisticPanel(simulation, simulation.localStayInSystem, "Local stay in system");
		simulation.getSimulationUpdated().add(panel);
		frame.addTab("Local stay", panel);
		
		panel = new StatisticPanel(simulation, simulation.globalStayInSystem, "Global stay in system");
		simulation.getReplicationEnded().add(panel);
		frame.addTab("Global stay", panel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
