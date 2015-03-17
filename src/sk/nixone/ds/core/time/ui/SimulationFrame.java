package sk.nixone.ds.core.time.ui;

import java.awt.AWTEventMulticaster;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;

import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.Statistic;
import sk.nixone.ds.core.time.AsyncTimeJumper;
import sk.nixone.ds.core.time.NiceProgressTimeJumper;
import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.SimpleTimeJumper;
import sk.nixone.ds.core.time.Simulation;
import sk.nixone.ds.core.time.Simulation.Observer;
import sk.nixone.ds.core.time.SimulationRun;
import sk.nixone.ds.core.time.TimeJumper;
import sk.nixone.ds.core.ui.NumberLabelEmitter;

public class SimulationFrame extends JFrame implements Observer {
	
	private JLabel timeDataLabel = new JLabel("Simulation time:");
	private JLabel timeDataNumber = new JLabel();
	private Emitter<Object, Double> timeEmitter = new DelayedEmitter<Object, Double>(new NumberLabelEmitter(timeDataNumber, 2), 50);

	private JLabel replicationLabel = new JLabel("Replication:");
	private JLabel replicationNumber = new JLabel();
	private Emitter<Object, Double> replicationEmitter = new DelayedEmitter<Object, Double>(new NumberLabelEmitter(replicationNumber), 50); 
	
	private JButton nextStepButton = new JButton("Next step");
	
	private JComboBox runTypeBox = new JComboBox(new String[]{ "Fast", "Steps", "Time" });
	
	private JComboBox timeFactorBox = new JComboBox(new String[]{ "1", "10^-1", "10^-2", "10^-3", "10^-4", "10^-5", "10^-7", "10^-10" });
	
	private JButton startButton = new JButton("Start");
	
	private JTabbedPane tabs = new JTabbedPane();
	
	private Simulation simulation;
	
	private Thread simulationThread = null;
	
	private AsyncTimeJumper buttonTimeJumper = new AsyncTimeJumper();
	
	public SimulationFrame(Simulation simulation) {
		super("Simulation");
		
		this.simulation = simulation;
		nextStepButton.setEnabled(false);
		
		setUpComponents();
		createLayout();
		
		simulation.addObserver(this);
	}
	
	public double getSelectedTimeFactor() {
		switch(timeFactorBox.getSelectedIndex()) {
		case 0: return 1;
		case 1: return 0.1;
		case 2: return 0.01;
		case 3: return 0.001;
		case 4: return 0.0001;
		case 5: return 0.00001;
		case 6: return 0.0000001;
		case 7: return 0.0000000001;
		}
		return 1;
	}
	
	public TimeJumper createSelectedTimeJumper() {
		switch(runTypeBox.getSelectedIndex()) {
		case 0: return new SimpleTimeJumper();
		case 1: return buttonTimeJumper;
		case 2: return new NiceProgressTimeJumper(getSelectedTimeFactor(), 0.1);
		}
		return null;
	}
	
	private void createLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(timeDataLabel)
						.addComponent(timeDataNumber)
						.addComponent(nextStepButton)
						.addComponent(runTypeBox)
						.addComponent(timeFactorBox)
						.addComponent(startButton)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(replicationLabel)
						.addComponent(replicationNumber)
						)
				.addComponent(tabs)
		);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(timeDataLabel)
						.addComponent(timeDataNumber)
						.addComponent(nextStepButton)
						.addComponent(runTypeBox)
						.addComponent(timeFactorBox)
						.addComponent(startButton)
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(replicationLabel)
						.addComponent(replicationNumber)
						)
				.addComponent(tabs)
		);
		
		tabs.setPreferredSize(new Dimension(600, 400));
		
		pack();
	}
	
	private void setUpComponents() {
		nextStepButton.setEnabled(false);
		nextStepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonTimeJumper.allow();
			}
		});
		
		timeFactorBox.setEnabled(false);
		runTypeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextStepButton.setEnabled(false);
				timeFactorBox.setEnabled(false);
				int selected = runTypeBox.getSelectedIndex();
				if(selected == 1) {
					nextStepButton.setEnabled(true);
				}
				if(selected == 2) {
					timeFactorBox.setEnabled(true);
				}
			}
		});
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryToStartSimulation();
			}
		});
	}

	public void addStatistic(String tabName, String dataName, Statistic statistic) {
		tabs.add(tabName, new StatisticPanel(simulation, statistic, dataName));
	}
	
	@Override
	public void onEventPlanned(SimulationRun run, PlannedEvent event) {}

	@Override
	public void onExecutedEvent(SimulationRun run, PlannedEvent executedEvent) {
		timeEmitter.emit(null, run.getCurrentSimulationTime());
	}

	@Override
	public void onVoidStep(SimulationRun run) {
		timeEmitter.emit(null, run.getCurrentSimulationTime());
	}

	@Override
	public void onSimulationStarted() {
		timeEmitter.reset();
		replicationEmitter.reset();
	}

	@Override
	public void onReplicationStarted(int replicationIndex, SimulationRun run) {
		replicationEmitter.emit(null, (double)replicationIndex);
		timeEmitter.emit(null, run.getCurrentSimulationTime());
	}

	@Override
	public void onReplicationEnded(int replicationIndex, SimulationRun run) {
		replicationEmitter.emit(null, (double)replicationIndex);
		timeEmitter.emit(null, run.getCurrentSimulationTime());
	}

	@Override
	public void onSimulationEnded() {
		replicationEmitter.reset();
		timeEmitter.reset();
	}
	
	public void tryToStartSimulation() {
		synchronized(this) {
			if(simulationThread != null) {
				return;
			}
			simulationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					simulation.run(createSelectedTimeJumper(), 1);
					synchronized(SimulationFrame.this) {
						simulationThread = null;
						startButton.setEnabled(true);
					}
				}
			});
			simulationThread.start();
			startButton.setEnabled(false);
		}
	}
}
