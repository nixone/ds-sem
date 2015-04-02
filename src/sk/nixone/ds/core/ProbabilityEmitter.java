package sk.nixone.ds.core;

/**
 * Emitter able to count probability of truthful boolean emits on itself.
 * 
 * @author nixone
 *
 */
public class ProbabilityEmitter implements Emitter<Boolean> {
	
	private Emitter<Double> emitter;

	private int allSamples = 0;

	private int positiveSamples = 0;
	
	/**
	 * Creates an empty probability emitter without any emitter to pass its computations to
	 */
	public ProbabilityEmitter() {
		this(null);
	}
	
	/**
	 * Creates an emitter that passes any probability update to specified emitter
	 * @param emitter emitter to pass the data to
	 */
	public ProbabilityEmitter(Emitter<Double> emitter) {
		this.emitter = emitter;
	}
	
	@Override
	public void reset() {
		allSamples = positiveSamples = 0;
		if (emitter != null) {
			emitter.reset();
		}
	}

	@Override
	public void emit(Boolean value) {
		if (value) {
			positiveSamples++;
		}
		allSamples++;
		if (emitter != null) {
			emitter.emit(getProbability());
		}
	}
	
	/**
	 * Returns the observed probability in the range between [0; 1]
	 * @return observed probability
	 */
	public double getProbability() {
		return (double)positiveSamples / allSamples;
	}

}
