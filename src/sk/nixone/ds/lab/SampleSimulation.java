package sk.nixone.ds.lab;

import java.util.LinkedList;

import sk.nixone.ds.core.Random;
import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.Statistic;
import sk.nixone.ds.core.time.Event;
import sk.nixone.ds.core.time.Simulation;
import sk.nixone.ds.core.time.SimulationRun;

/**
 *
 * @author olesnanik2
 */
public class SampleSimulation extends Simulation {
	
	static private double LAMBDA_CUSTOMER_TIME = 1/100.;
	static private double LAMBDA_PROCESS_TIEM = 1/45.;
	
	public class Customer {
		private double startedTime;
		private double processStartedTime;
		private double endedTime;
	}
	
	public class CustomerArrived implements Event {
		private Customer customer;
		
		@Override
		public void execute(SimulationRun run) {
			customer = new Customer();
			customer.startedTime = run.getCurrentSimulationTime();
			
			if (processingCustomer == null) {
				CustomerStarted started = new CustomerStarted();
				started.customer = customer;
				run.planImmediately(started);
			} else {
				queueLength.add(customerQueue.size() / (run.getCurrentSimulationTime() - lastQueueLengthTime));
				lastQueueLengthTime = run.getCurrentSimulationTime();
				
				customerQueue.addLast(customer);
			}
			
			CustomerArrived arrived = new CustomerArrived();
			run.plan(customerArrivalRandom.nextExponential(LAMBDA_CUSTOMER_TIME), arrived);
		}
		
		public String toString() {
			return "Customer arrived";
		}
	}
	
	public class CustomerStarted implements Event {
		private Customer customer;
		
		@Override
		public void execute(SimulationRun run) {
			customer.processStartedTime = run.getCurrentSimulationTime();
			
			customerWaitingTime.add(customer.processStartedTime - customer.startedTime);
			
			processingCustomer = customer;
			CustomerFinished finished = new CustomerFinished();
			finished.customer = customer;
			run.plan(processDurationRandom.nextExponential(LAMBDA_PROCESS_TIEM), finished);
		}
		
		public String toString() {
			return "Customer started";
		}
	}
	
	public class CustomerFinished implements Event {
		public Customer customer;
		
		@Override
		public void execute(SimulationRun run) {
			customer.endedTime = run.getCurrentSimulationTime();
			
			customerInSystemTime.add(customer.endedTime - customer.startedTime);
			customerProcessTime.add(customer.endedTime - customer.processStartedTime);
			
			processingCustomer = null;
			if(!customerQueue.isEmpty()) {
				queueLength.add(customerQueue.size() / (run.getCurrentSimulationTime() - lastQueueLengthTime));
				lastQueueLengthTime = run.getCurrentSimulationTime();
				
				Customer customer = customerQueue.removeFirst();
				CustomerStarted started = new CustomerStarted();
				started.customer = customer;
				run.planImmediately(started);
			}
		}
		
		public String toString() {
			return "Customer finished";
		}
	}
	
	private Customer processingCustomer = null;
	private LinkedList<Customer> customerQueue = new LinkedList<>();
	
	private Random customerArrivalRandom;
	private Random processDurationRandom;
	
	private Statistic customerWaitingTime = new Statistic();
	private Statistic customerInSystemTime = new Statistic();
	private Statistic customerProcessTime = new Statistic();
	private Statistic queueLength = new Statistic();
	private double lastQueueLengthTime;
	private Statistic maximalQueueLength = new Statistic();
	
	public SampleSimulation(Randoms randoms) {
		customerArrivalRandom = randoms.getNextRandom();
		processDurationRandom = randoms.getNextRandom();
	}

	@Override
	public void initializeRun(SimulationRun run) {
		customerQueue.clear();
		processingCustomer = null;
		lastQueueLengthTime = run.getCurrentSimulationTime();
		
		run.setMaximumSimulationTime(3600);
		run.plan(customerArrivalRandom.nextExponential(LAMBDA_CUSTOMER_TIME), new CustomerArrived());
	}
	
	public Statistic getCustomerInSystemTime() {
		return customerInSystemTime;
	}
	
	public Statistic getCustomerProcessTime() {
		return customerProcessTime;
	}
	
	public Statistic getCustomerWaitingTime() {
		return customerWaitingTime;
	}
	
	public Statistic getQueueLength() {
		return queueLength;
	}
	
	public Statistic getMaximalQueueLength() {
		return maximalQueueLength;
	}
}
