package sk.nixone.ds.core.time.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sk.nixone.ds.core.NumberUtil;
import sk.nixone.ds.core.time.ControllableTimeJumper;
import sk.nixone.ds.core.time.SimpleTimeJumper;
import sk.nixone.ds.core.time.Simulation;
import sk.nixone.ds.core.time.SimulationConfig;
import sk.nixone.ds.core.time.TimeJumper;

public class SimulationController extends JPanel {
	
	private ControllableTimeJumper controlledTimeJumper = new ControllableTimeJumper();
	private SimpleTimeJumper simpleTimeJumper = new SimpleTimeJumper();
	private Thread simulationThread = null;
	
	private JLabel replicationsLabel = new JLabel("Replications:");
	private JTextField replicationsCount = new JTextField("100");
	private JCheckBox observeBox = new JCheckBox("I want to observe");
	private TimeJumperController jumperController = new TimeJumperController(controlledTimeJumper);
	private JButton startButton = new JButton("Start");
	private JButton stopButton = new JButton("Stop");
	
	private Simulation simulation;
	
	public SimulationController(Simulation simulation) {
		super();
		this.simulation = simulation;
		
		createComponents();
		createLayout();
	}
	
	private SimulationConfig createSelectedConfig() {
		TimeJumper jumper = null;
		if (observeBox.isSelected()) {
			jumper = controlledTimeJumper;
		} else {
			jumper = simpleTimeJumper;
		}
		
		SimulationConfig config = new SimulationConfig(jumper, NumberUtil.readBig(replicationsCount.getText()));
		config.setIgnoreRunImmediateEmitters(!observeBox.isSelected());
		config.setIgnoreImmediateEmitters(!observeBox.isSelected());
		
		return config;
	}
	
	private void createComponents() {
		observeBox.setSelected(true);
		stopButton.setEnabled(false);
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryToStart();
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulation simulation = SimulationController.this.simulation;
				if(simulation != null) {
					simulation.stop();
				}
			}
		});
		
		observeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jumperController.setVisible(createSelectedConfig().getJumper() == controlledTimeJumper);
			}
		});
	}
	
	private void createLayout() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(replicationsLabel)
						.addComponent(replicationsCount)
						)
				.addComponent(observeBox)
				.addComponent(jumperController)
				.addComponent(startButton)
				.addComponent(stopButton)
				);
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(replicationsLabel)
						.addComponent(replicationsCount)
						)
				.addComponent(observeBox)
				.addComponent(jumperController)
				.addComponent(startButton)
				.addComponent(stopButton)
				);
	}
	
	private void tryToStart() {
		synchronized(this) {
			if(simulationThread != null) {
				return;
			}
			final SimulationConfig config = createSelectedConfig();
			simulationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					simulation.run(config);
					synchronized(SimulationController.this) {
						simulationThread = null;
					}
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
				}
			});
			simulationThread.start();
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
		}
	}
}
