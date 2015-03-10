package sk.nixone.ds.lab;

import java.util.LinkedList;
import sk.nixone.ds.core.Random;
import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.time.Event;
import sk.nixone.ds.core.time.SimulationRun;

/**
 *
 * @author olesnanik2
 */
public class SampleSimulationRun extends SimulationRun {
	
	static private double LAMBDA_CUSTOMER_TIME = 1/45f;
	static private double LAMBDA_PROCESS_TIEM = 1/100f;
	
	public class Customer {
		private double startedTime;
		private double processStartedTime;
		private double endedTime;
	}
	
	public class CustomerArrived implements Event {
		private Customer customer;
		
		@Override
		public void execute() {
			customer = new Customer();
			customer.startedTime = getCurrentSimulationTime();
			
			if (processingCustomer == null) {
				CustomerStarted started = new CustomerStarted();
				started.customer = customer;
				planImmediately(started);
			} else {
				customerQueue.addLast(customer);
			}
			
			CustomerArrived arrived = new CustomerArrived();
			plan(customerArrivalRandom.nextExponential(LAMBDA_CUSTOMER_TIME), arrived);
		}
	}
	
	public class CustomerStarted implements Event {
		private Customer customer;
		
		@Override
		public void execute() {
			customer.processStartedTime = getCurrentSimulationTime();
			processingCustomer = customer;
			CustomerFinished finished = new CustomerFinished();
			finished.customer = customer;
			plan(processDurationRandom.nextExponential(LAMBDA_PROCESS_TIEM), finished);
		}
	}
	
	public class CustomerFinished implements Event {
		public Customer customer;
		
		@Override
		public void execute() {
			customer.endedTime = getCurrentSimulationTime();
			
			processingCustomer = null;
			if(!customerQueue.isEmpty()) {
				Customer customer = customerQueue.removeFirst();
				CustomerStarted started = new CustomerStarted();
				started.customer = customer;
				planImmediately(started);
			}
		}
	}
	
	private Customer processingCustomer = null;
	private LinkedList<Customer> customerQueue = new LinkedList<>();
	
	private Random customerArrivalRandom;
	private Random processDurationRandom;
	
	public SampleSimulationRun(Randoms randoms) {
		customerArrivalRandom = randoms.getNextRandom();
		processDurationRandom = randoms.getNextRandom();
		
		plan(customerArrivalRandom.nextExponential(LAMBDA_CUSTOMER_TIME), new CustomerArrived());
	}
}
