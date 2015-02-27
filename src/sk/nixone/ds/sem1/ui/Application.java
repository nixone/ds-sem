package sk.nixone.ds.sem1.ui;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.sem1.Game;
import sk.nixone.ds.sem1.GameSimulation;

public class Application {
	
	static public void main(String [] arguments) {
		GameSimulation simulation = new GameSimulation(new Game(new Randoms()));
		
		MainFrame frame = new MainFrame(simulation);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
