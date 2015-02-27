package sk.nixone.ds.sem1;

import sk.nixone.ds.core.Random;
import sk.nixone.ds.core.Randoms;

public class Game {

	enum Strategy {
		BEST, RANDOM;
	}
	
	static final private double[] ACCURACIES = {
		0.6, 0.7, 0.75, 0.25, 0.10, 0.90
	};
	
	static final private int[] PLAYERS_FROM_BEST = {
		5, 2, 1, 0, 3, 4
	};
	
	static final public int PLAYER_COUNT = ACCURACIES.length;
	
	private boolean [] isAlive = null;
	private Random [] shootRandoms = null;
	private Random [] selectionRandoms = null;
	private int alivePlayers = 0;
	
	private int [] shooties = null;
	
	private Strategy strategy = Strategy.BEST;
	
	public Game(Randoms randoms) {
		isAlive = new boolean[PLAYER_COUNT];
		shooties = new int[PLAYER_COUNT];
		
		this.strategy = strategy;
		this.shootRandoms = new Random[PLAYER_COUNT];
		this.selectionRandoms = new Random[PLAYER_COUNT];
		
		for(int p=0; p<PLAYER_COUNT; p++) {
			this.shootRandoms[p] = randoms.getNextRandom();
			this.selectionRandoms[p] = randoms.getNextRandom();
		}
		
		reset();
	}
	
	private int selectShootie(int shooter) {
		if(strategy == Strategy.BEST) {
			return getBestShootie(shooter);
		} else {
			return getRandomShootieFor(shooter);
		}
	}
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
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
	
	public int getBestShootie(int shooter) {
		for(int p=0; p<PLAYER_COUNT; p++) {
			int b = PLAYERS_FROM_BEST[p];
			
			if(isAlive[b] && shooter != b) {
				return b;
			}
		}
		return -1;
	}
	
	private void makeShootieSelection() {
		for(int p=0; p<PLAYER_COUNT; p++) {
			shooties[p] = isAlive[p] ? selectShootie(p) : -1;
		}
	}
	
	private boolean isFinished() {
		return alivePlayers <= 1;
	}
	
	private void makeSelectedShoot() {
		for(int p=0; p<PLAYER_COUNT; p++) {
			if(shooties[p] != -1) {
				makeShoot(p, shooties[p]);
			}
		}
	}

	private void makeShoot(int shooter, int shootie) {
		double guess = shootRandoms[shooter].nextDouble();
		if(guess <= ACCURACIES[shooter]) {
			if(isAlive[shootie]) {
				alivePlayers--;
			}
			isAlive[shootie] = false;
		}
	}
	
	public void reset() {
		for(int p=0; p<PLAYER_COUNT; p++) {
			isAlive[p] = true;
		}
		alivePlayers = PLAYER_COUNT;
	}
	
	public boolean[] getResult() {
		return isAlive;
	}
	
	public void execute() {
		while(!isFinished()) {
			makeShootieSelection();
			makeSelectedShoot();
		}
	}
	
	public boolean isPlayerAlive(int n) {
		return isAlive[n];
	}
	
	public boolean areAllDead() {
		return alivePlayers == 0;
	}
}
