package sk.nixone.ds.core.time.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import sk.nixone.ds.core.time.ControllableTimeJumper;

public class TimeJumperController extends JPanel {

	private JLabel timeFactorLabel = new JLabel("Time factor:");
	private JComboBox timeFactorBox = new JComboBox(new String[]{ "1", "10^-1", "10^-2", "10^-3", "10^-4", "10^-5", "10^-7", "10^-10" });
	private JButton pauseButton = new JButton("Pause");
	private JButton nextStepButton = new JButton("Next step");
	private boolean paused = false;
	
	private ControllableTimeJumper jumper;
	
	public TimeJumperController(ControllableTimeJumper jumper) {
		super();
		this.jumper = jumper;
		
		createComponents();
		createLayout();
		
		jumper.setTimeFactor(getSelectedTimeFactor());
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
		}
		return 1;
	}
	
	private void createComponents() {
		nextStepButton.setEnabled(false);
		nextStepButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jumper.nextStep();
			}
		});
		
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!paused) {
					pauseButton.setText("Unpause");
					jumper.setPaused(true);
					nextStepButton.setEnabled(true);
					paused = true;
				} else {
					pauseButton.setText("Pause");
					jumper.setPaused(false);
					nextStepButton.setEnabled(false);
					paused = false;
				}
			}
		});
		
		timeFactorBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jumper.setTimeFactor(getSelectedTimeFactor());
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
				.addComponent(nextStepButton)
				.addComponent(pauseButton)
				);
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(timeFactorLabel)
						.addComponent(timeFactorBox)
						)
				.addComponent(nextStepButton)
				.addComponent(pauseButton)
				);
	}
}
