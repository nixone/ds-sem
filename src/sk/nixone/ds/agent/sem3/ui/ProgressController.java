package sk.nixone.ds.agent.sem3.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import sk.nixone.ds.agent.sem3.Simulation;

public class ProgressController extends JPanel {

	private JLabel timeFactorLabel = new JLabel("Time factor:");
	private JComboBox timeFactorBox = new JComboBox(new String[]{ "1", "10^-1", "10^-2", "10^-3", "10^-4", "10^-5", "10^-7", "10^-10", "10^-12" });
	private JButton pauseButton = new JButton("Pause");
	private boolean paused = false;
	
	private Simulation simulation;
	
	public ProgressController(Simulation simulation) {
		super();
		
		this.simulation = simulation;
		
		createComponents();
		createLayout();
		
		simulation.setTimeFactor(getSelectedTimeFactor());
	}
	
	private double getSelectedTimeFactor() {
		switch(timeFactorBox.getSelectedIndex()) {
		case 0: return 1;
		case 1: return 0.1;
		case 2: return 0.01;
		case 3: return 0.001;
		case 4: return 0.0001;
		case 5: return 0.00001;
		case 6: return 0.0000001;
		case 7: return 0.0000000001;
		case 8: return 0.000000000001;
		}
		return 1;
	}
	
	private void createComponents() {
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!paused) {
					pauseButton.setText("Unpause");
					simulation.pause();
					paused = true;
				} else {
					pauseButton.setText("Pause");
					simulation.unpause();
					paused = false;
				}
			}
		});
		
		timeFactorBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.setTimeFactor(getSelectedTimeFactor());
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
						.addComponent(timeFactorLabel)
						.addComponent(timeFactorBox)
						)
				.addComponent(pauseButton)
				);
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(timeFactorLabel)
						.addComponent(timeFactorBox)
						)
				.addComponent(pauseButton)
				);
	}
}
