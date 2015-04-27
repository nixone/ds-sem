package sk.nixone.ds.agent.sem3.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sk.nixone.ds.agent.sem3.Simulation;
import sk.nixone.util.NumberUtil;

public class SimulationController extends JPanel {
	
	private Thread simulationThread = null;
	
	private JLabel replicationsLabel = new JLabel("Replications:");
	private JTextField replicationsCount = new JTextField("100");
	private JCheckBox observeBox = new JCheckBox("I want to observe");
	private ProgressController progressController;
	private JButton startButton = new JButton("Start");
	private JButton stopButton = new JButton("Stop");
	
	private Simulation simulation;
	
	public SimulationController(Simulation simulation) {
		super();
		this.simulation = simulation;
		progressController = new ProgressController(simulation);
		
		createComponents();
		createLayout();
	}
	
	public MouseWheelListener getMouseWheelListener() {
		return progressController.getMouseWheelListener();
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
				progressController.setVisible(observeBox.isSelected());
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
				.addComponent(progressController)
				.addComponent(startButton)
				.addComponent(stopButton)
				);
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(replicationsLabel)
						.addComponent(replicationsCount)
						)
				.addComponent(observeBox)
				.addComponent(progressController)
				.addComponent(startButton)
				.addComponent(stopButton)
				);
	}
	
	private void tryToStart() {
		synchronized(this) {
			if(simulationThread != null) {
				return;
			}
			
			final boolean fast = !observeBox.isSelected();
			final int replications = NumberUtil.readBig(replicationsCount.getText());
			
			simulationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					simulation.run(replications);
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
