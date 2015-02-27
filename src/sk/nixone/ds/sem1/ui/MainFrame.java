package sk.nixone.ds.sem1.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import sk.nixone.ds.sem1.Game;
import sk.nixone.ds.sem1.GameSimulation;

public class MainFrame extends JFrame {
	
	private JLabel strategyLabel = new JLabel("Strategy:");
	private ButtonGroup strategyGroup = new ButtonGroup();
	private JRadioButton strategyRandomButton = new JRadioButton("Random");
	private JRadioButton strategyBestButton = new JRadioButton("Best");
	private JLabel replicationNumberLabel = new JLabel("Replications:");
	private JTextField replicationNumberInput = new JTextField();
	private JLabel displayDataCountLabel = new JLabel("Show records:");
	private JTextField displayDataCountInput = new JTextField();
	private JButton startButton = new JButton("Simulate");
	
	private JTabbedPane tabs = new JTabbedPane();
	
	private GameSimulation simulation;
	
	public MainFrame(GameSimulation simulation) {
		super("1. simulácia - Semestrálna práca - Martin Olešnaník");
		
		this.simulation = simulation;
		
		createComponents();
		createLayout();
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createComponents() {
		replicationNumberInput.setText("10000000");
		displayDataCountInput.setText("1000000");
		
		strategyGroup.add(strategyBestButton);
		strategyBestButton.setSelected(true);
		strategyGroup.add(strategyRandomButton);
		
		tabs.addTab("All dead", new TabContent("All dead", simulation.getAllDeadObserver()));
		for(int p=0; p<Game.PLAYER_COUNT; p++) {
			String name = (char)('A'+p)+" stayed";
			tabs.addTab(name, new TabContent(name, simulation.getPlayerStayedAliveObserver(p)));
		}
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startSimulation();
			}
		});
	}
	
	private void createLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(strategyLabel)
						.addComponent(strategyBestButton)
						.addComponent(strategyRandomButton)
						.addComponent(replicationNumberLabel)
						.addComponent(replicationNumberInput)
						.addComponent(displayDataCountLabel)
						.addComponent(displayDataCountInput)
						.addComponent(startButton)
						)
				.addComponent(tabs)
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(strategyLabel)
						.addComponent(strategyBestButton)
						.addComponent(strategyRandomButton)
						.addComponent(replicationNumberLabel)
						.addComponent(replicationNumberInput)
						.addComponent(displayDataCountLabel)
						.addComponent(displayDataCountInput)
						.addComponent(startButton)
						)
				.addComponent(tabs)
				);
	}
	
	private void startSimulation() {
		final int replicationCount = Integer.parseInt(replicationNumberInput.getText());
		final int displayDataCount = Integer.parseInt(displayDataCountInput.getText());
		final int refreshUIEvery = 50000;
		
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				simulation.run(replicationCount, refreshUIEvery);
			}
		}).start();
	}
}
