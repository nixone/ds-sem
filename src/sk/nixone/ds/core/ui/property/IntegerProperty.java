package sk.nixone.ds.core.ui.property;

public class IntegerProperty extends LabelTextProperty {
	
	public IntegerProperty(String name) {
		this(name, 0);
	}
	
	public IntegerProperty(String name, int defaultValue) {
		super(name, String.valueOf(defaultValue));
	}

	public int getValue() {
		return Integer.parseInt(getLastValue());
	}
	
	public void setValue(int value) {
		setValue(String.valueOf(value));
	}
}
