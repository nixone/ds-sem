package sk.nixone.ds.core.ui.property;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class LabelTextProperty implements Property {
	
	private JLabel label;
	private JTextField editor;
	private String lastValue;
	
	public LabelTextProperty(String name) {
		this(name, "");
	}
	
	public LabelTextProperty(String name, String defaultValue) {
		label = new JLabel(name+":");
		lastValue = defaultValue;
		editor = new JTextField(defaultValue);
	}
	
	public void setValue(String value) {
		editor.setText(lastValue = value);
	}
	
	public String getLastValue() {
		return lastValue;
	}

	@Override
	public Component getLabel() {
		return label;
	}

	@Override
	public Component getEditor() {
		return editor;
	}
}
