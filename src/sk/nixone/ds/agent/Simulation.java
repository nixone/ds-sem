package sk.nixone.ds.agent;

import sk.nixone.ds.core.Emitters;
import OSPABA.ISimDelegate;
import OSPABA.SimState;

public abstract class Simulation extends sk.nixone.ds.core.statik.Simulation {
	
	private Emitters<SimState> simulationStateChanged = new Emitters<SimState>();
	private Emitters<Object> refreshInvoked = new Emitters<Object>();
	private SimulationRun currentSimulationRun = null;
	private double timeFactor = Double.NaN;
	
	private ISimDelegate delegate = new ISimDelegate() {
		@Override
		public void simStateChanged(OSPABA.Simulation sim, SimState state) {
			simulationStateChanged.emit(state);
		}
		
		@Override
		public void refresh(OSPABA.Simulation sim) {
			refreshInvoked.emit(null);
		}
	};
	
	protected abstract SimulationRun createSimulationRun();
	
	public Emitters<SimState> getSimulationStateChanged() {
		return simulationStateChanged;
	}
	
	public Emitters<Object> getRefreshInvoked() {
		return refreshInvoked;
	}
	
	public void runReplication() {
		currentSimulationRun = createSimulationRun();
		currentSimulationRun.registerDelegate(delegate);
		refreshSimulationSpeed();
		currentSimulationRun.simulate();
		currentSimulationRun = null;
	}
	
	public void setTimeFactor(double timeFactor) {
		this.timeFactor = timeFactor;
		refreshSimulationSpeed();
	}
	
	public void pause() {
		SimulationRun run = currentSimulationRun;
		if (run != null) {
			run.pauseSimulation();
		}
	}
	
	public void unpause() {
		SimulationRun run = currentSimulationRun;
		if (run != null) {
			run.resumeSimulation();
		}
	}
	
	public double getSimulationTime() {
		SimulationRun run = currentSimulationRun;
		if (run == null) {
			return 0;
		}
		return run.currentTime();
	}
	
	private void refreshSimulationSpeed() {
		SimulationRun run = currentSimulationRun;
		if (run != null) {
			if (timeFactor != Double.NaN) {
				run.setSimSpeed((1./timeFactor)/25, 1./25);
			} else {
				run.setMaxSimSpeed();
			}
		}
	}
}
