package sk.nixone.ds.agent.sem3.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.agent.sem3.model.Schedule;
import sk.nixone.ds.agent.sem3.model.VehicleType;
import sk.nixone.util.HashablePair;
import sk.nixone.util.NumberUtil;

public class ConfigPanel extends JPanel {
	
	private Model model;
	
	private HashMap<HashablePair<Line, VehicleType>, JTextField> inputFields = new HashMap<HashablePair<Line, VehicleType>, JTextField>();
	private HashMap<HashablePair<Line, VehicleType>, JTextArea> inputLabels = new HashMap<HashablePair<Line, VehicleType>, JTextArea>();	
	
	private HashMap<Line, JLabel> lineLabels = new HashMap<Line, JLabel>();
	
	private HashMap<VehicleType, JLabel> vehicleTypeLabels = new HashMap<VehicleType, JLabel>();
	
	private JPanel schedulesPanel;
	
	private JLabel strategyLabel = new JLabel("Bus leave strategy:");
	private JComboBox<String> strategyChooser = new JComboBox<String>(new String[]{"Immediate departure", "1.5 min delayed departure"});
	
	private JLabel priceLabel = new JLabel("Solution price:");
	private JLabel priceNumber = new JLabel();
	
	private JLabel helpLabel = new JLabel("distribute:Vehicles | list:T1[m] t2[m]... | static:Start[m] Offset[m] Vehicles ");
	
	private JPanel summaryPanel;
	
	public ConfigPanel(Model model) {
		super();
		
		this.model = model;
		
		createComponents();
		createLayout();
		
		setSelectedStrategy();
		loadScheduleDescriptions();
	}
	
	private void setSelectedStrategy() {
		int index = strategyChooser.getSelectedIndex();
		model.setWaitingStrategy(index == 1);
		loadScheduleDescriptions();
		reloadSchedulesData();
		recount();
	}
	
	private void createComponents() {
		createSchedulesComponents();
		createSchedulesLayout();
		
		createSummaryComponents();
		createSummaryLayout();
	}
	
	private void createLayout() {
		summaryPanel.setPreferredSize(new Dimension(200, 100));
		schedulesPanel.setPreferredSize(new Dimension(200, 400));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(summaryPanel)
				.addComponent(schedulesPanel)
				.addComponent(helpLabel)
				);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(summaryPanel)
				.addComponent(schedulesPanel)
				.addComponent(helpLabel)
				);
	}
	
	private void createSummaryComponents() {
		priceNumber.setFont(priceNumber.getFont().deriveFont(20f));
		
		strategyChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSelectedStrategy();
			}
		});
	}
	
	private void createSummaryLayout() {
		GridBagLayout layout = new GridBagLayout();
		summaryPanel = new JPanel(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = c.weighty = 1;
		c.insets.top = c.insets.bottom = c.insets.left = c.insets.right = 5;
		
		c.gridy = 0; c.gridx = 0;
		summaryPanel.add(strategyLabel, c);
		c.gridx = 1;
		summaryPanel.add(strategyChooser, c);
		c.gridy = 1; c.gridx = 0;
		summaryPanel.add(priceLabel, c);
		c.gridx = 1;
		summaryPanel.add(priceNumber, c);
	}
	
	private void createSchedulesComponents() {
		for(VehicleType type : model.getVehicleTypes()) {
			vehicleTypeLabels.put(type, new JLabel(type.getName()));
		}
		
		for(Line line : model.getLines()) {
			lineLabels.put(line, new JLabel(line.getName()));
			for(VehicleType type : model.getVehicleTypes()) {
				HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(line, type);
				inputFields.put(p, createTextField(p));
				inputLabels.put(p, createTextArea());
			}
		}
	}
	
	private void createSchedulesLayout() {
		GridBagLayout layout = new GridBagLayout();
		schedulesPanel = new JPanel(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(10, 10, 10, 10);
		
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridy = 1;
		for(VehicleType type : model.getVehicleTypes()) {
			c.gridx = 0;
			schedulesPanel.add(vehicleTypeLabels.get(type), c);
			c.gridy += 2;
		}
		
		c.gridx = 1;
		for(Line line : model.getLines()) {
			c.gridy = 0;
			schedulesPanel.add(lineLabels.get(line), c);
			c.gridx++;
		}
		
		c.gridy = 1;
		c.weightx = 3;
		c.weighty = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		for(VehicleType type : model.getVehicleTypes()) {
			c.gridx = 1;
			for(Line line : model.getLines()) {
				HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(line, type);
				schedulesPanel.add(inputFields.get(p), c);
				c.gridy++;
				schedulesPanel.add(inputLabels.get(p), c);
				c.gridy--;
				
				c.gridx++;
			}
			c.gridy += 2;
		}
	}
	
	private JTextField createTextField(final HashablePair<Line, VehicleType> pair) {
		final JTextField field = new JTextField();
		field.setMinimumSize(new Dimension(200, 50));
		field.setHorizontalAlignment(JTextField.CENTER);
		
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
				Schedule schedule = model.findSchedule(pair.getFirst(), pair.getSecond());
				onTextFieldChanged(field, schedule);
			}
		});
		
		return field;
	}
	
	private JTextArea createTextArea() {
		JTextArea area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setSize(200, 50);
		return area;
	}
	
	private void onTextFieldChanged(JTextField field, Schedule schedule) {
		schedule.fromExpression(field.getText());
		reloadSchedulesData();
		recount();
	}
	
	private void loadScheduleDescriptions() {
		for(HashablePair<Line, VehicleType> pair : inputFields.keySet()) {
			Schedule schedule = model.findSchedule(pair.getFirst(), pair.getSecond());
			inputFields.get(pair).setText(schedule.getExpression());
		}
	}
	
	private void reloadSchedulesData() {
		for(HashablePair<Line, VehicleType> pair : inputLabels.keySet()) {
			JTextArea area = inputLabels.get(pair);
			final Schedule schedule = model.findSchedule(pair.getFirst(), pair.getSecond());
			String content = "";
			for(double seconds : schedule) {
				double minutes = seconds/60.;
				content += String.format("%.1f  ", minutes);
			}
			area.setText(content);
		}
	}
	
	private void recount() {
		priceNumber.setText(NumberUtil.nicePrice(model.getPrice())+"Kč");
	}
}
