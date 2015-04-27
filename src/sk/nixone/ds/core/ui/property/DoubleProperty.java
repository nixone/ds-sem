package sk.nixone.ds.core.ui.property;

public class DoubleProperty extends LabelTextProperty {
	
	public DoubleProperty(String name) {
		this(name, 0.0);
	}
	
	public DoubleProperty(String name, double defaultValue) {
		super(name, String.valueOf(defaultValue));
	}

	public double getValue() {
		return Double.parseDouble(getLastValue());
	}
	
	public void setValue(double value) {
		setValue(String.valueOf(value));
	}
}
