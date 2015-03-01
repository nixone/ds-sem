package sk.nixone.ds.sem1;

import sk.nixone.ds.core.Random;
import sk.nixone.ds.core.Randoms;

/**
 * Reprezentacia momentalneho stavu hry, priebehu a jej pravidiel.
 * 
 * @author nixone
 *
 */
public class Game {

	/**
	 * Strategia, ktoru mozu hraci volit.
	 * 
	 * @author nixone
	 *
	 */
	public enum Strategy {
		BEST, RANDOM;
	}
	
	/**
	 * Zoznam presnosti, ktore maju hraci 0..5
	 */
	static final private double[] ACCURACIES = {
		0.6, 0.7, 0.75, 0.25, 0.10, 0.90
	};
	
	/**
	 * Indexy hracov od najlepsieho (tykajuc sa presnosti) po najhorsieho
	 */
	static final private int[] PLAYERS_FROM_BEST = {
		5, 2, 1, 0, 3, 4
	};
	
	/**
	 * Pocet hracov pritomnych na zaciatku hry
	 */
	static final public int PLAYER_COUNT = ACCURACIES.length;
	
	/**
	 * Pole popisujuce, ktori hraci su momentalne zivi
	 */
	private boolean [] isAlive = null;
	
	/**
	 * Pole generatorov, ktore sa ku kazdemu hracovi pouzije na generovanie uspesnosti zasahu.
	 */
	private Random [] shootRandoms = null;
	
	/**
	 * Pole generatorov, ktore sa ku kazdemu hracovi pouzije na generovanie nahodneho hraca, ktoreho dany hrac zastreli.
	 */
	private Random [] selectionRandoms = null;
	
	/**
	 * Pocet momentalne zijucich hracov v hre.
	 */
	private int alivePlayers = 0;
	
	/**
	 * Pole momentalne vybranych cielov pre kazdeho hraca.
	 */
	private int [] shooties = null;
	
	/**
	 * Momentalne nastavena strategia vsetkych hracov.
	 */
	private Strategy strategy = Strategy.BEST;
	
	/**
	 * Pomocou generatora generatorov vytvori novu hru, ktora moze byt vsak vykonana na simulacie viacero priebehov hry, pokial
	 * je spravne znovuinicializovana pomocou metody <code>reset()</code>.
	 * @param randoms
	 */
	public Game(Randoms randoms) {
		isAlive = new boolean[PLAYER_COUNT];
		shooties = new int[PLAYER_COUNT];
		
		this.shootRandoms = new Random[PLAYER_COUNT];
		this.selectionRandoms = new Random[PLAYER_COUNT];
		
		for(int p=0; p<PLAYER_COUNT; p++) {
			this.shootRandoms[p] = randoms.getNextRandom();
			this.selectionRandoms[p] = randoms.getNextRandom();
		}
		
		reset();
	}
	
	/**
	 * Na zaklade momentalnej strateige vyberie ciel pre daneho strelca
	 * @param shooter index strelca
	 * @return index cielu
	 */
	private int selectShootie(int shooter) {
		if(strategy == Strategy.BEST) {
			return getBestShootie(shooter);
		} else {
			return getRandomShootieFor(shooter);
		}
	}
	
	/**
	 * Nastavi strategiu vyberu ciela pre vsetkych hracov
	 * @param strategy strategia
	 */
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	/**
	 * Nahodne vyberie ciel pre daneho hraca.
	 * 
	 * Ciel je vyberany nahodne (rovnomernym rozdelenim pravdpeodobnosti) zo vsetkych momentalne zijucich hracov.
	 * 
	 * @param shooter index strelca
	 * @return index cielu
	 */
	public int getRandomShootieFor(int shooter) {
		int shootieNumber = selectionRandoms[shooter].nextInt(alivePlayers-1);
		
		int aliveShootie = 0;
		for(int p=0; p<PLAYER_COUNT; p++) {
			if(!isAlive[p] || p == shooter) {
				continue;
			}
			if(aliveShootie++ == shootieNumber) {
				return p;
			}
		}
		
		return -1;
	}
	
	/**
	 * Vyberie ciel pre daneho hraca na zaklade toho, ktory z ostatnych zijucich hracov je najlepsi strelec.
	 * 
	 * @param shooter index strelca
	 * @return index cielu
	 */
	public int getBestShootie(int shooter) {
		for(int p=0; p<PLAYER_COUNT; p++) {
			int b = PLAYERS_FROM_BEST[p];
			
			if(isAlive[b] && shooter != b) {
				return b;
			}
		}
		return -1;
	}
	
	/**
	 * Pre kazdeho ziveho strelca vyberie urcity ciel
	 */
	private void makeShootieSelection() {
		for(int p=0; p<PLAYER_COUNT; p++) {
			shooties[p] = isAlive[p] ? selectShootie(p) : -1;
		}
	}
	
	/**
	 * Indikuje koncovy stav hry
	 * 
	 * @return true, ak je hra na konci, false inak
	 */
	private boolean isFinished() {
		return alivePlayers <= 1;
	}
	
	/**
	 * Podla momentalne vybranych cielov, simuluje vystrel kazdeho ziveho strelca
	 */
	private void makeSelectedShoot() {
		for(int p=0; p<PLAYER_COUNT; p++) {
			if(shooties[p] != -1) {
				makeShoot(p, shooties[p]);
			}
		}
	}

	/**
	 * Simuluje vystrel jedneho strelca na jeden ciel
	 * 
	 * @param shooter index strelca
	 * @param shootie index ciela
	 */
	private void makeShoot(int shooter, int shootie) {
		double guess = shootRandoms[shooter].nextDouble();
		if(guess <= ACCURACIES[shooter]) {
			if(isAlive[shootie]) {
				alivePlayers--;
			}
			isAlive[shootie] = false;
		}
	}
	
	/**
	 * Znovuinicializuje hru, aby bola v stave pred zacatim novej hry.
	 */
	public void reset() {
		for(int p=0; p<PLAYER_COUNT; p++) {
			isAlive[p] = true;
		}
		alivePlayers = PLAYER_COUNT;
	}
	
	/**
	 * Simuluje cely priebeh hry az po koncovy stav.
	 */
	public void execute() {
		while(!isFinished()) {
			makeShootieSelection();
			makeSelectedShoot();
		}
	}
	
	/**
	 * Zisti, ci v momentalnom stave hry je nazive urcity hrac
	 * @param n index zistovaneho hraca
	 * @return true, ak je hrac nazive, false inak
	 */
	public boolean isPlayerAlive(int n) {
		return isAlive[n];
	}
	
	/**
	 * Zisti, ci v momentalnom stave hry su mrtvi vsetci hraci
	 * @return su vsetci hraci mrtvi?
	 */
	public boolean areAllDead() {
		return alivePlayers == 0;
	}
}
