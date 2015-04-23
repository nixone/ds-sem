package sk.nixone.ds.agent.sem3;

import java.io.File;
import java.io.IOException;

import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.Randoms;

public class Application {
	public static void main(String[] args) throws IOException {
		Randoms randoms = new Randoms();
		
		Model model = new Model(new File("input.txt"));
		model.prepare(randoms);
		
		Simulation simulation = new Simulation(randoms, model);
		simulation.run(1);
	}
}
