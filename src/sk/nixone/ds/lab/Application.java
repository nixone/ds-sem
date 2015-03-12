package sk.nixone.ds.lab;

import java.io.IOException;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.NiceProgressTimeJumper;
import sk.nixone.ds.core.time.ui.EventCalendarFrame;

/**
 *
 * @author olesnanik2
 */
public class Application {
	public static void main(String [] arguments) throws IOException {
		EventCalendarFrame frame = new EventCalendarFrame();
		frame.setVisible(true);
		
		SampleSimulationRun run = new SampleSimulationRun(new Randoms());
		run.addObserver(frame);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				run.run(new NiceProgressTimeJumper(0.1, 0.5));
			}
		}).start();
	}
}
