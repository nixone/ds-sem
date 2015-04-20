package sk.nixone.ds.agent.sem3;

import java.io.File;
import java.io.IOException;

import sk.nixone.ds.core.Randoms;

public class Application {
	public static void main(String[] args) throws IOException {
		ModelInput input = new ModelInput(new File("input.txt"));
		
		Simulation simulation = new Simulation(new Randoms(), input);
		simulation.run(1);
	}
}
