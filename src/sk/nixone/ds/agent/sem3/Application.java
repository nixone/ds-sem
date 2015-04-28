package sk.nixone.ds.agent.sem3;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.ui.SimulationFrame;
import sk.nixone.ds.agent.sem3.ui.StationsLayout;
import sk.nixone.ds.core.Randoms;
import sk.nixone.util.AppearanceUtil;

public class Application {
	public static void main(String[] args) throws IOException {
		File linesFile = new File("data/lines.txt");
		File stationLayoutFile = new File("data/station_layout.txt");
		final File schedulesFile = new File("data/schedules.txt");
		
		Randoms randoms = new Randoms();
		
		final Model model = new Model(linesFile, randoms);
		if(schedulesFile.exists()) {
			model.loadSchedules(schedulesFile);
		}
		
		StationsLayout stationsLayout = new StationsLayout(stationLayoutFile);
		
		Simulation simulation = new Simulation(randoms, model);
		
		SimulationFrame frame = new SimulationFrame(simulation, model, stationsLayout);
		frame.setDefaultCloseOperation(SimulationFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					model.saveSchedules(schedulesFile);
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
}
