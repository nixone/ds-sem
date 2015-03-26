package sk.nixone.ds.core.time.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
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
	private JLabel typeLabel = new JLabel("Speed:");
	private JComboBox typeBox = new JComboBox(new String[]{ "Controlled", "Fast", "Super fast" });
	private TimeJumperController jumperController = new TimeJumperController(controlledTimeJumper);
	private JButton startButton = new JButton("Start");
	
	private Simulation simulation;
	
	public SimulationController(Simulation simulation) {
		super();
		this.simulation = simulation;
		
		createComponents();
		createLayout();
	}
	
	private SimulationConfig createSelectedConfig() {
		TimeJumper jumper = null;
		if (typeBox.getSelectedIndex() == 0) {
			jumper = controlledTimeJumper;
		} else {
			jumper = simpleTimeJumper;
		}
		
		SimulationConfig config = new SimulationConfig(jumper, NumberUtil.readBig(replicationsCount.getText()));
		config.setIgnoreRunImmediateEmitters(typeBox.getSelectedIndex() >= 1);
		config.setIgnoreImmediateEmitters(typeBox.getSelectedIndex() >= 2);
		
		return config;
	}
	
	private void createComponents() {
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tryToStart();
			}
		});
		
		typeBox.addActionListener(new ActionListener() {
			
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
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(typeLabel)
						.addComponent(typeBox)
						)
				.addComponent(jumperController)
				.addComponent(startButton)
				);
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(replicationsLabel)
						.addComponent(replicationsCount)
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(typeLabel)
						.addComponent(typeBox)
						)
				.addComponent(jumperController)
				.addComponent(startButton)
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
				}
			});
			simulationThread.start();
			startButton.setEnabled(false);
		}
	}
}
