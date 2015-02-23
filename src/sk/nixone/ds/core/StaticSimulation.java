package sk.nixone.ds.core;

import java.util.LinkedList;
import java.util.List;

public class StaticSimulation<T> {

	public interface Thrower<T> {
		public void reset();
		public T doThrow();
	}
	
	public interface Observer<T> {
		public void reset();
		public void doObserve(T result);
	}
	
	private Thrower<T> thrower = null;
	private List<Observer<T>> observers = null;
	
	public StaticSimulation() {
		this.observers = new LinkedList<Observer<T>>();
	}
	
	public void setThrower(Thrower<T> thrower) {
		this.thrower = thrower;
	}
	
	public void addObserver(Observer<T> observer) {
		this.observers.add(observer);
	}
	
	public void run(int replications) {
		thrower.reset();
		for(Observer<T> observer : observers) {
			observer.reset();
		}
		
		for(int replication=0; replication<replications; replication++) {
			T result = thrower.doThrow();
			for(Observer<T> observer : observers) {
				observer.doObserve(result);
			}
		}
	}
}
