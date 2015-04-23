package sk.nixone.ds.agent.sem3.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout.Alignment;

import sk.nixone.ds.agent.sem3.Simulation;
import sk.nixone.ds.core.DelayedEmitter;
import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.ui.NumberLabelEmitter;
import sk.nixone.ds.core.ui.TimeLabelEmitter;

@SuppressWarnings("serial")
public abstract class SimulationFrameBase extends JFrame {
	
	private JLabel timeDataLabel = new JLabel("Simulation time:");
	private JLabel timeDataNumber = new JLabel();
	private Emitter<Double> timeEmitter = new DelayedEmitter<Double>(new TimeLabelEmitter(timeDataNumber), 15);

	private JLabel replicationLabel = new JLabel("Replication:");
	private JLabel replicationNumber = new JLabel();
	private Emitter<Double> replicationEmitter = new DelayedEmitter<Double>(new NumberLabelEmitter(replicationNumber), 15); 
	
	private SimulationController controller;
	
	private JTabbedPane tabs = new JTabbedPane();
	
	public SimulationFrameBase(final Simulation simulation) {
		super("Simulation");
		
		controller = new SimulationController(simulation);
		
		setUpComponents();
		createLayout();
		
		Emitter<Object> updater = new Emitter<Object>() {
			
			@Override
			public void reset() {
				timeEmitter.reset();
				replicationEmitter.reset();
			}
			
			@Override
			public void emit(Object value) {
				timeEmitter.emit(simulation.getSimulationTime());
				replicationEmitter.emit((double)simulation.getCurrentReplicationNumber());
			}
		};
		
		simulation.getRefreshInvoked().add(updater);
	}
	
	private void createLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addGroup(layout.createSequentialGroup()
								.addComponent(replicationLabel)
								.addComponent(replicationNumber)
								)
						.addGroup(layout.createSequentialGroup()
								.addComponent(timeDataLabel)
								.addComponent(timeDataNumber)
								)
						.addComponent(controller)
						)
				.addComponent(tabs)
		);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.CENTER)
								.addComponent(replicationLabel)
								.addComponent(replicationNumber)
								)
						.addGroup(layout.createParallelGroup(Alignment.CENTER)
								.addComponent(timeDataLabel)
								.addComponent(timeDataNumber)
								)
						.addComponent(controller)
						)
				.addComponent(tabs)
		);
		
		tabs.setPreferredSize(new Dimension(1000, 600));
		
		pack();
	}
	
	private void setUpComponents() {
		replicationNumber.setFont(replicationNumber.getFont().deriveFont(Font.BOLD));
		timeDataNumber.setFont(timeDataNumber.getFont().deriveFont(Font.BOLD));
	}
	
	public void addTab(String tabName, Component component) {
		tabs.add(tabName, component);
	}
}
