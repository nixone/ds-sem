package sk.nixone.ds.lab;

import java.io.IOException;
import java.io.InputStreamReader;
import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.SimulationRun;

/**
 *
 * @author olesnanik2
 */
public class Application {
	public static void main(String [] arguments) throws IOException {
		SimulationRun.Observer observer = new SimulationRun.Observer() {
			@Override
			public void onEventPlanned(SimulationRun run, PlannedEvent event) {
				System.out.printf("[%f][Planned] %s.\n", run.getCurrentSimulationTime(), event.getEvent().toString());
			}

			@Override
			public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent) {
				System.out.printf("[%f][Executed] %s.\n", run.getCurrentSimulationTime(), executedEvent.getEvent().toString());
			}

			@Override
			public void onVoidStep(SimulationRun run) {
				System.out.printf("[%f][Void]\n", run.getCurrentSimulationTime());
			}
		};
		
		SampleSimulationRun run = new SampleSimulationRun(new Randoms());
		run.addObserver(observer);
		
		InputStreamReader reader = new InputStreamReader(System.in);
		
		while(true) {
			reader.read();
			run.nextStep();
		}
	}
}
