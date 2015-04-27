package sk.nixone.ds.agent.sem3;

import java.io.File;
import java.io.IOException;

import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.ui.SimulationFrame;
import sk.nixone.ds.agent.sem3.ui.StationsLayout;
import sk.nixone.ds.core.Randoms;
import sk.nixone.util.AppearanceUtil;

public class Application {
	public static void main(String[] args) throws IOException {
		AppearanceUtil.setNiceSwingLookAndFeel();
		
		Randoms randoms = new Randoms();
		
		Model model = new Model(new File("model.txt"), randoms);
		
		StationsLayout stationsLayout = new StationsLayout(new File("layout.txt"));
		
		Simulation simulation = new Simulation(randoms, model);
		
		SimulationFrame frame = new SimulationFrame(simulation, model, stationsLayout);
		frame.setDefaultCloseOperation(SimulationFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
