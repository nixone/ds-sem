package sk.nixone.ds.agent.sem3.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Schedule;
import sk.nixone.ds.agent.sem3.model.VehicleType;
import sk.nixone.util.HashablePair;

public class ConfigPanel extends JPanel {
	
	private Model model;
	
	private HashMap<HashablePair<Line, VehicleType>, JTextField> textFields = new HashMap<HashablePair<Line, VehicleType>, JTextField>();
	
	private HashMap<Line, JLabel> lineLabels = new HashMap<Line, JLabel>();
	
	private HashMap<VehicleType, JLabel> vehicleTypeLabels = new HashMap<VehicleType, JLabel>();
	
	public ConfigPanel(Model model) {
		super();
		
		this.model = model;
		
		createComponents();
		createLayout();
	}
	
	private void createComponents() {
		for(VehicleType type : model.getVehicleTypes()) {
			vehicleTypeLabels.put(type, new JLabel(type.getName()));
		}
		
		for(Line line : model.getLines()) {
			lineLabels.put(line, new JLabel(line.getName()));
			for(VehicleType type : model.getVehicleTypes()) {
				HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(line, type);
				textFields.put(p, createTextField(p));
			}
		}
	}
	
	private void createLayout() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridy = 1;
		for(VehicleType type : model.getVehicleTypes()) {
			c.gridx = 0;
			add(vehicleTypeLabels.get(type), c);
			c.gridy++;
		}
		
		c.gridx = 1;
		for(Line line : model.getLines()) {
			c.gridy = 0;
			add(lineLabels.get(line), c);
			c.gridx++;
		}
		
		
		c.gridy = 1;
		for(VehicleType type : model.getVehicleTypes()) {
			c.gridx = 1;
			for(Line line : model.getLines()) {
				HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(line, type);
				add(textFields.get(p), c);
				c.gridx++;
			}
			c.gridy++;
		}
	}
	
	private JTextField createTextField(HashablePair<Line, VehicleType> pair) {
		final JTextField field = new JTextField();
		final Schedule schedule = model.findSchedule(pair.getFirst(), pair.getSecond());
		String content = "";
		for(double seconds : schedule) {
			int minutes = (int)Math.round(seconds/60.);
			content += minutes+" ";
		}
		field.setText(content);
		
		field.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				changed();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changed();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				changed();
			}
			
			private void changed() {
				onTextFieldChanged(field, schedule);
			}
		});
		
		return field;
	}
	
	private void onTextFieldChanged(JTextField field, Schedule schedule) {
		String content = field.getText();
		
		schedule.clear();
		
		Scanner scanner = new Scanner(content);
		while(scanner.hasNextDouble()) {
			double seconds = scanner.nextDouble() * 60;
			schedule.add(seconds);
		}
		scanner.close();
	}
}
