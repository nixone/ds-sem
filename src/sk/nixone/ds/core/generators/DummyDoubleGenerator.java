package sk.nixone.ds.core.generators;

public class DummyDoubleGenerator implements Generator<Double> {

	private double dummy;
	
	public DummyDoubleGenerator(double dummy) {
		this.dummy = dummy;
	}
	
	@Override
	public Double next() {
		return dummy;
	}

}
