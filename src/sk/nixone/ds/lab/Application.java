package sk.nixone.ds.lab;

import java.io.IOException;

import javax.swing.JFrame;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.SimpleTimeJumper;
import sk.nixone.ds.core.time.ui.SimulationFrame;
import sk.nixone.util.AppearanceUtil;

/**
 *
 * @author olesnanik2
 */
public class Application {
	public static void main(String [] arguments) throws IOException {
		AppearanceUtil.setNiceSwingLookAndFeel();
		
		final SampleSimulation simulation = new SampleSimulation(new Randoms());
		
		SimulationFrame frame = new SimulationFrame(simulation);
		frame.addStatistic("T. in system", "Time customer spent in system", simulation.getCustomerInSystemTime());
		frame.addStatistic("Process t.", "Process time", simulation.getCustomerProcessTime());
		frame.addStatistic("Wait t.", "Waiting time for start", simulation.getCustomerWaitingTime());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
