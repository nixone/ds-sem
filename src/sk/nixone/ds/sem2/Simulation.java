package sk.nixone.ds.sem2;

import java.util.ArrayList;
import java.util.List;

import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.Statistic;
import sk.nixone.ds.core.generators.ExponentialDelayGenerator;
import sk.nixone.ds.core.generators.Generator;
import sk.nixone.ds.core.generators.OccurenceGenerator;
import sk.nixone.ds.core.generators.TriangleGenerator;
import sk.nixone.ds.core.generators.UniformGenerator;
import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.SimulationRun;

public class Simulation extends sk.nixone.ds.core.time.Simulation {

	static public final double MODEL_DURATION = 86400;
	
	Generator<Double> travelerArrivalGenerator;
	Generator<Boolean> hasLuggageGenerator;
	Generator<Boolean> needsPersonalCheckupGenerator;
	Generator<Double> checkupDurationGenerator;
	Generator<Double> luggageScanDurationGenerator;
	Generator<Double> personalCheckupDurationGenerator;
	
	List<TravelerWithLuggageQueue> travellersWithLuggage = new ArrayList<TravelerWithLuggageQueue>(2);
	List<BeforeLuggageQueue> beforeLuggageQueues = new ArrayList<BeforeLuggageQueue>(2);
	Luggage [] processingLuggage = new Luggage[2];
	PlannedEvent [] processingEvents = new PlannedEvent[2];
	List<AfterLuggageQueue> afterLuggageQueues = new ArrayList<AfterLuggageQueue>(2);
	
	List<BeforeCheckupQueue> beforeCheckupQueues = new ArrayList<BeforeCheckupQueue>(2);
	Traveler [] checkingTravelers = new Traveler[2];
	PlannedEvent [] detectorEvents = new PlannedEvent[2];
	PlannedEvent [] checkupEvents = new PlannedEvent[2];
	PlannedEvent arrivalEvent = null;
	
	int finishedTravelers = 0;
	
	Randoms randoms;
	
	Statistic localStayInSystem = new Statistic();
	Statistic globalStayInSystem = new Statistic();
	Statistic servedPeople = new Statistic();
	
	public Simulation(Randoms randoms) {
		this.randoms = randoms;
		travelerArrivalGenerator = new ExponentialDelayGenerator(randoms.getNextRandom(), 864f);
		hasLuggageGenerator = new OccurenceGenerator(randoms.getNextRandom(), 0.88);
		needsPersonalCheckupGenerator = new OccurenceGenerator(randoms.getNextRandom(), 0.13);
		checkupDurationGenerator = new UniformGenerator(randoms.getNextRandom(), 7, 17);
		luggageScanDurationGenerator = new UniformGenerator(randoms.getNextRandom(), 10, 57);
		personalCheckupDurationGenerator = new TriangleGenerator(randoms.getNextRandom(), 10, 25, 120);
		
		for(int i=0; i<2; i++) {
			travellersWithLuggage.add(new TravelerWithLuggageQueue(this, i));
			beforeLuggageQueues.add(new BeforeLuggageQueue(this, i));
			processingLuggage[i] = null;
			afterLuggageQueues.add(new AfterLuggageQueue(this, i));
			
			beforeCheckupQueues.add(new BeforeCheckupQueue(this, i));
			checkingTravelers[i] = null;
		}
	}
	
	public void setEstimatedCapacity(int people) {
		travelerArrivalGenerator = new ExponentialDelayGenerator(randoms.getNextRandom(), 86400./people);
	}
	
	public void setBeforeLimit(int limit) {
		beforeLuggageQueues.get(0).setMaximum(limit);
		beforeLuggageQueues.get(1).setMaximum(limit);
	}
	
	public void setAfterLimit(int limit) {
		afterLuggageQueues.get(0).setMaximum(limit);
		afterLuggageQueues.get(1).setMaximum(limit);
	}
	
	public int getShorterLineIndex() {
		if (travellersWithLuggage.get(0).size() <= travellersWithLuggage.get(1).size()) {
			return 0;
		}
		return 1;
	}
	
	public void finish(Traveler traveler) {
		finishedTravelers++;
		localStayInSystem.add(traveler.stayInSystem.getDuration());
	}

	@Override
	public void onReplicationStart(int replicationIndex) {
		localStayInSystem.clear();
		finishedTravelers = 0;
	}

	@Override
	public void onReplicationEnd(int replicationIndex) {
		if(replicationIndex > 0) {
			servedPeople.add(finishedTravelers);
			globalStayInSystem.add(localStayInSystem.getMean());
		}

	}

	@Override
	public void onStarted() {
		globalStayInSystem.clear();
		localStayInSystem.clear();
		finishedTravelers = 0;
		for(int i=0; i<2; i++) {
			travellersWithLuggage.get(i).clear();
			beforeLuggageQueues.get(i).clear();
			processingLuggage[i] = null;
			afterLuggageQueues.get(i).clear();
			beforeCheckupQueues.get(i).clear();
			checkingTravelers[i] = null;
			
			checkupEvents[i] = null;
			detectorEvents[i] = null;
			processingEvents[i] = null;
			arrivalEvent = null;
		}
	}

	@Override
	public void onEnded() {}

	@Override
	public void replan(SimulationRun original, SimulationRun newOne) {
		if(original == null) {
			newOne.plan(travelerArrivalGenerator.next(), new TravelerArrived(this));
		} else {
			original.replanInto(newOne, MODEL_DURATION);
		}
		newOne.setMaximumSimulationTime(MODEL_DURATION);
	}
}
