package sk.nixone.ds.sem2;

import javax.swing.JFrame;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.ui.SimulationFrame;
import sk.nixone.util.AppearanceUtil;

public class Application {
	public static void main(String[] args) {
		AppearanceUtil.setNiceSwingLookAndFeel();
		
		final Simulation simulation = new Simulation(new Randoms(1));
		
		SimulationFrame frame = new SimulationFrame(simulation);
		frame.addTab("Display", new SimulationPanel(simulation));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
