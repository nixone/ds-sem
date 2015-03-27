package sk.nixone.ds.sem2;

import java.util.ArrayList;
import java.util.List;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.generators.ExponentialDelayGenerator;
import sk.nixone.ds.core.generators.Generator;
import sk.nixone.ds.core.generators.OccurenceGenerator;
import sk.nixone.ds.core.generators.TriangleGenerator;
import sk.nixone.ds.core.generators.UniformGenerator;
import sk.nixone.ds.core.time.SimulationRun;

public class Simulation extends sk.nixone.ds.core.time.Simulation {

	double latestArrivalTime = 86400;
	
	Generator<Double> travelerArrivalGenerator;
	Generator<Boolean> hasLuggageGenerator;
	Generator<Boolean> needsPersonalCheckupGenerator;
	Generator<Double> checkupDurationGenerator;
	Generator<Double> luggageScanDurationGenerator;
	Generator<Double> personalCheckupDurationGenerator;
	
	List<TravelerWithLuggageQueue> travellersWithLuggage = new ArrayList<TravelerWithLuggageQueue>(2);
	List<BeforeLuggageQueue> beforeLuggageQueues = new ArrayList<BeforeLuggageQueue>(2);
	Luggage [] processingLuggage = new Luggage[2];
	
	
	public Simulation(Randoms randoms) {
		// TODO Ask about mean in this generator
		travelerArrivalGenerator = new ExponentialDelayGenerator(randoms.getNextRandom(), 100f);
		hasLuggageGenerator = new OccurenceGenerator(randoms.getNextRandom(), 0.88);
		needsPersonalCheckupGenerator = new OccurenceGenerator(randoms.getNextRandom(), 0.13);
		checkupDurationGenerator = new UniformGenerator(randoms.getNextRandom(), 7, 17);
		luggageScanDurationGenerator = new UniformGenerator(randoms.getNextRandom(), 10, 57);
		personalCheckupDurationGenerator = new TriangleGenerator(randoms.getNextRandom(), 10, 25, 120);
	
		for(int i=0; i<2; i++) {
			travellersWithLuggage.add(new TravelerWithLuggageQueue(this, i));
			processingLuggage[i] = null;
		}
	}
	
	@Override
	public void initializeRun(SimulationRun run) {

	}
	
	public int getShorterLineIndex() {
		if (travellersWithLuggage.get(0).size() <= travellersWithLuggage.get(1).size()) {
			return 0;
		}
		return 1;
	}
}
