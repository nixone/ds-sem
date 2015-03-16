package sk.nixone.ds.lab;

import java.io.IOException;

import javax.swing.JFrame;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.AsyncTimeJumper;
import sk.nixone.ds.core.time.NiceProgressTimeJumper;
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
		
		SampleSimulation simulation = new SampleSimulation(new Randoms());
		
		SimulationFrame frame = new SimulationFrame(simulation);
		frame.addStatistic("In system", "Time of customer in system", simulation.getCustomerInSystemTime());
		frame.addStatistic("Process time", "Time of customer being processed", simulation.getCustomerProcessTime());
		frame.addStatistic("Wait time", "Time of customer waiting", simulation.getCustomerWaitingTime());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//final AsyncTimeJumper jumper = new AsyncTimeJumper();
		//frame.setAsyncTimeJumper(jumper);
		//final NiceProgressTimeJumper jumper = new NiceProgressTimeJumper(0.0001, 1);
		final SimpleTimeJumper jumper = new SimpleTimeJumper();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				simulation.run(jumper, 1);
			}
		}).start();
	}
}
