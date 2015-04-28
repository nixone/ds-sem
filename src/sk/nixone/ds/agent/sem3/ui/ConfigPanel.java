package sk.nixone.ds.agent.sem3.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	
	private HashMap<HashablePair<Line, VehicleType>, JTextField> textFields = new HashMap<HashablePair<Line, VehicleType>, JTextField>();
	
	private HashMap<Line, JLabel> lineLabels = new HashMap<Line, JLabel>();
	
	private HashMap<VehicleType, JLabel> vehicleTypeLabels = new HashMap<VehicleType, JLabel>();
	
	private JPanel schedulesPanel;
	
	private JLabel strategyLabel = new JLabel("Bus leave strategy:");
	private JComboBox<String> strategyChooser = new JComboBox<String>(new String[]{"Immediate departure", "1.5 min delayed departure"});
	
	private JLabel priceLabel = new JLabel("Solution price:");
	private JLabel priceNumber = new JLabel();
	
	private JPanel summaryPanel;
	
	public ConfigPanel(Model model) {
		super();
		
		this.model = model;
		
		createComponents();
		createLayout();
		
		setSelectedStrategy();
	}
	
	private void setSelectedStrategy() {
		int index = strategyChooser.getSelectedIndex();
		model.setWaitingStrategy(index == 1);
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
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(summaryPanel)
				.addComponent(schedulesPanel)
				);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(summaryPanel)
				.addComponent(schedulesPanel)
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
				textFields.put(p, createTextField(p));
			}
		}
	}
	
	private void createSchedulesLayout() {
		GridBagLayout layout = new GridBagLayout();
		schedulesPanel = new JPanel(layout);
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridy = 1;
		for(VehicleType type : model.getVehicleTypes()) {
			c.gridx = 0;
			schedulesPanel.add(vehicleTypeLabels.get(type), c);
			c.gridy++;
		}
		
		c.gridx = 1;
		for(Line line : model.getLines()) {
			c.gridy = 0;
			schedulesPanel.add(lineLabels.get(line), c);
			c.gridx++;
		}
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 1;
		for(VehicleType type : model.getVehicleTypes()) {
			c.gridx = 1;
			for(Line line : model.getLines()) {
				HashablePair<Line, VehicleType> p = new HashablePair<Line, VehicleType>(line, type);
				schedulesPanel.add(textFields.get(p), c);
				c.gridx++;
			}
			c.gridy++;
		}
	}
	
	private JTextField createTextField(final HashablePair<Line, VehicleType> pair) {
		final JTextField field = new JTextField();
		
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
	
	private void onTextFieldChanged(JTextField field, Schedule schedule) {
		String content = field.getText();
		
		schedule.clear();
		
		Scanner scanner = new Scanner(content);
		while(scanner.hasNextDouble()) {
			double seconds = scanner.nextDouble() * 60;
			schedule.add(seconds);
		}
		scanner.close();

		recount();
	}
	
	private void reloadSchedulesData() {
		for(HashablePair<Line, VehicleType> pair : textFields.keySet()) {
			JTextField field = textFields.get(pair);
			final Schedule schedule = model.findSchedule(pair.getFirst(), pair.getSecond());
			String content = "";
			for(double seconds : schedule) {
				int minutes = (int)Math.round(seconds/60.);
				content += minutes+" ";
			}
			field.setText(content);
		}
	}
	
	private void recount() {
		priceNumber.setText(NumberUtil.nicePrice(model.getPrice())+"Kč");
	}
}
