package sk.nixone.ds.lab;

import java.io.IOException;

import javax.swing.JFrame;

import sk.nixone.ds.core.Randoms;
import sk.nixone.util.AppearanceUtil;

/**
 *
 * @author olesnanik2
 */
public class Application {
	public static void main(String [] arguments) throws IOException {
		AppearanceUtil.setNiceSwingLookAndFeel();
		
		final SampleSimulation simulation = new SampleSimulation(new Randoms());
		
		SampleSimulationFrame frame = new SampleSimulationFrame(simulation);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
