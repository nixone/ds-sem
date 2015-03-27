package sk.nixone.ds.sem2;

import java.util.LinkedList;

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
	
	public void add(SimulationRun run, T item) {
		if (isFull()) {
			throw new RuntimeException("isFull");
		}
		internal.addLast(item);
	}
	
	public T peek() {
		return internal.getFirst();
	}
	
	public T remove(SimulationRun run) {
		if (isEmpty()) {
			throw new RuntimeException("isEmpty");
		}
		return internal.removeFirst();
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
}
