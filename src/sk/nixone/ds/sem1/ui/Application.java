package sk.nixone.ds.sem1.ui;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.sem1.Game;
import sk.nixone.ds.sem1.GameSimulation;

/**
 * Trieda ktora obshauje vstupny bod celej simulacie a uzivatelskeho rozhrania.
 * 
 * @author nixone
 *
 */
public class Application {
	
	/**
	 * Vstupny bod aplikacie. Aplikacia neprijma ziadne vstupne parametre (ak su dodane, budu ignorovane)
	 * @param arguments vstupne argumenty
	 */
	static public void main(String [] arguments) {
		GameSimulation simulation = new GameSimulation(new Game(new Randoms()));
		
		MainFrame frame = new MainFrame(simulation);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
