package sk.nixone.ds.core.time.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout.Alignment;

import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.Statistic;
import sk.nixone.ds.core.time.AsyncTimeJumper;
import sk.nixone.ds.core.time.PlannedEvent;
import sk.nixone.ds.core.time.Simulation;
import sk.nixone.ds.core.time.Simulation.Observer;
import sk.nixone.ds.core.time.SimulationRun;
import sk.nixone.ds.core.ui.NumberLabelEmitter;

public class SimulationFrame extends JFrame implements Observer {
	
	private JLabel timeDataLabel = new JLabel("Simulation time:");
	private JLabel timeDataNumber = new JLabel();
	private Emitter<Object, Double> timeEmitter = new DelayedEmitter<Object, Double>(new NumberLabelEmitter(timeDataNumber, 2), 50);

	private JLabel replicationLabel = new JLabel("Replication:");
	private JLabel replicationNumber = new JLabel();
	private Emitter<Object, Double> replicationEmitter = new DelayedEmitter<Object, Double>(new NumberLabelEmitter(replicationNumber), 50); 
	
	private JButton nextStepButton = new JButton("Next step");
	
	private JTabbedPane tabs = new JTabbedPane();
	
	private Simulation simulation;
	
	public SimulationFrame(Simulation simulation) {
		super("Simulation");
		
		this.simulation = simulation;
		nextStepButton.setEnabled(false);
		
		createLayout();
		
		simulation.addObserver(this);
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
	
	public void setAsyncTimeJumper(final AsyncTimeJumper jumper) {
		nextStepButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jumper.allow();
			}
		});
		nextStepButton.setEnabled(true);
	}
	
}
