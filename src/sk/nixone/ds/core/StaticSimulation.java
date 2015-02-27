package sk.nixone.ds.core;

import java.util.Collection;
import java.util.HashSet;

import javax.swing.SwingUtilities;

public abstract class StaticSimulation {
	
	private Collection<ValueObserver<?>> observers = new HashSet<ValueObserver<?>>();
	
	public void reset() {
		for(ValueObserver<?> observer : observers) {
			observer.reset();
		}
	}
	
	public abstract void runReplication();
	
	public void addObserver(ValueObserver<?> observer) {
		observers.add(observer);
	}
	
	public void removeObserver(ValueObserver<?> observer) {
		observers.remove(observer);
	}
	
	public void run(int replications, int updateUIEvery, int cropReplications) {
		reset();
		for(int replication=0; replication<replications; replication++) {
			runReplication();
			updateObservers(replication);
			if(replication % updateUIEvery == 0 && replication >= cropReplications) {
				updateObserversUI(replication);
			}
		}
	}
	
	private void updateObservers(int r) {
		for(ValueObserver<?> observer : observers) {
			observer.update(r);
		}
	}
	
	private void updateObserversUI(int r) {
		for(ValueObserver<?> o : observers) {
			o.updateUI(r);
		}
	}
}
