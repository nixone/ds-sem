package sk.nixone.ds.sem2;

import java.util.LinkedList;

import sk.nixone.ds.core.time.Event;
import sk.nixone.ds.core.time.SimulationRun;

public class Queue<T> {
	
	final protected int line;
	final protected Simulation simulation;
	
	private int maximum = 0;
	
	private LinkedList<T> internal = new LinkedList<T>();
	
	public Queue(Simulation simulation, int line) {
		this.simulation = simulation;
		this.line = line;
	}
	
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	
	public void add(SimulationRun run, final T item) {
		if (isFull()) {
			throw new RuntimeException("isFull");
		}
		internal.addLast(item);
		
		run.planImmediately(new Event() {
			@Override
			public void execute(SimulationRun run) {
				onItemAdded(run, item);
			}
		});
	}
	
	public void onItemAdded(SimulationRun run, T item) {
	}
	
	public void onItemRemoved(SimulationRun run, T item) {
	}
	
	public void remove(SimulationRun run, final T item) {
		internal.remove(item);
		run.planImmediately(new Event() {
			@Override
			public void execute(SimulationRun run) {
				onItemRemoved(run, item);
			}
		});
	}
	
	public T peek() {
		return internal.getFirst();
	}
	
	public T remove(SimulationRun run) {
		if (isEmpty()) {
			throw new RuntimeException("isEmpty");
		}
		final T item = internal.removeFirst();
		run.planImmediately(new Event() {
			@Override
			public void execute(SimulationRun run) {
				onItemRemoved(run, item);
			}
		});
		return item;
	}
	
	public int size() {
		return internal.size();
	}
	
	public boolean isEmpty() {
		return internal.isEmpty();
	}
	
	public boolean isFull() {
		return maximum > 0 && internal.size() == maximum;
	}
	
	public void clear() {
		internal.clear();
	}
}
