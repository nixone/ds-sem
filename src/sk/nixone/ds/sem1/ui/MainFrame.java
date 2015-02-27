package sk.nixone.ds.sem1.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import sk.nixone.ds.core.NumberUtil;
import sk.nixone.ds.sem1.Game;
import sk.nixone.ds.sem1.GameSimulation;

public class MainFrame extends JFrame {
	
	private JLabel strategyLabel = new JLabel("Strategy:");
	private ButtonGroup strategyGroup = new ButtonGroup();
	private JRadioButton strategyRandomButton = new JRadioButton("Random");
	private JRadioButton strategyBestButton = new JRadioButton("Best");
	private JLabel replicationNumberLabel = new JLabel("Replications:");
	private JTextField replicationNumberInput = new JTextField();
	private JLabel dataPercentageLabel = new JLabel("Show %:");
	private JSlider dataPercentageSlider = new JSlider(0, 1000, 500);
	private JLabel cropPercentageLabel = new JLabel("Crop %:");
	private JSlider cropPercentageSlider = new JSlider(0, 1000, 50);
	private JButton startButton = new JButton("Simulate");
	
	private JTabbedPane tabs = new JTabbedPane();
	
	private GameSimulation simulation;
	
	private volatile boolean isSimulationRunning = false;
	
	private HashSet<TabContent> tabContents = new HashSet<TabContent>();
	
	public MainFrame(GameSimulation simulation) {
		super("1st semestral work - Martin Olešnaník - nixone.sk");
		
		this.simulation = simulation;
		
		createComponents();
		createLayout();
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createComponents() {
		replicationNumberInput.setText("100m");
		
		strategyGroup.add(strategyBestButton);
		strategyBestButton.setSelected(true);
		strategyGroup.add(strategyRandomButton);

		TabContent tabContent = new TabContent("All dead", simulation.getAllDeadObserver());
		tabs.addTab("All dead", tabContent);
		tabContents.add(tabContent);
		for(int p=0; p<Game.PLAYER_COUNT; p++) {
			String name = (char)('A'+p)+" stayed";
			tabContent = new TabContent(name, simulation.getPlayerStayedAliveObserver(p));
			tabs.addTab(name, tabContent);
			tabContents.add(tabContent);
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
						.addComponent(cropPercentageLabel)
						.addComponent(cropPercentageSlider)
						.addComponent(dataPercentageLabel)
						.addComponent(dataPercentageSlider)
						.addComponent(startButton)
						)
				.addComponent(tabs)
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(strategyLabel)
						.addComponent(strategyBestButton)
						.addComponent(strategyRandomButton)
						.addComponent(replicationNumberLabel)
						.addComponent(replicationNumberInput)
						.addComponent(cropPercentageLabel)
						.addComponent(cropPercentageSlider)
						.addComponent(dataPercentageLabel)
						.addComponent(dataPercentageSlider)
						.addComponent(startButton)
						)
				.addComponent(tabs)
				);
	}
	
	private void startSimulation() {
		if(isSimulationRunning) {
			return;
		}
		isSimulationRunning = true;
		startButton.setEnabled(false);
		
		final int replicationCount = NumberUtil.readBig(replicationNumberInput.getText());
		double percentageToShow = (double)dataPercentageSlider.getValue() / dataPercentageSlider.getMaximum();
		final int refreshUIEvery = 20000 < replicationCount ? 20000 : 1;
		int maxDataPointsToShow = (int)(replicationCount * percentageToShow / refreshUIEvery) + 1;  
		final int cropReplications = (int)(replicationCount * ((double)cropPercentageSlider.getValue() / cropPercentageSlider.getMaximum()));
		simulation.getGame().setStrategy(strategyBestButton.isSelected() ? Game.Strategy.BEST : Game.Strategy.RANDOM);
		
		
		for(TabContent tabContent : tabContents) {
			tabContent.setMaximumDataPoints(maxDataPointsToShow);
		}
		
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				simulation.run(replicationCount, refreshUIEvery, cropReplications);
				
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						isSimulationRunning = false;
						startButton.setEnabled(true);
					}
				});
			}
		}).start();
	}
}
