package sk.nixone.ds.core.time;

public class SimulationConfig {
	
	private TimeJumper jumper;
	private int replications;
	private boolean ignoreImmediateEmitters = false;
	private boolean ignoreRunImmediateEmitters = false;
	
	public SimulationConfig(TimeJumper jumper, int replications) {
		this.jumper = jumper;
		this.replications = replications;
	}
	
	public TimeJumper getJumper() {
		return jumper;
	}

	public void setJumper(TimeJumper jumper) {
		this.jumper = jumper;
	}

	public int getReplications() {
		return replications;
	}

	public void setReplications(int replications) {
		this.replications = replications;
	}

	public boolean isIgnoreImmediateEmitters() {
		return ignoreImmediateEmitters;
	}

	public void setIgnoreImmediateEmitters(boolean ignoreImmediateEmitters) {
		this.ignoreImmediateEmitters = ignoreImmediateEmitters;
	}

	public boolean isIgnoreRunImmediateEmitters() {
		return ignoreRunImmediateEmitters;
	}

	public void setIgnoreRunImmediateEmitters(boolean ignoreRunImmediateEmitters) {
		this.ignoreRunImmediateEmitters = ignoreRunImmediateEmitters;
	}
}
