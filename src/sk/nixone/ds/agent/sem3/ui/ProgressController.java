package sk.nixone.ds.agent.sem3.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sk.nixone.ds.agent.sem3.Simulation;

public class ProgressController extends JPanel {

	private JLabel timeFactorLabel = new JLabel("Time factor:");
	private JSlider timeFactorSlider = new JSlider(0, 1000);
	private JLabel timeFactorNumber = new JLabel("");
	private JButton pauseButton = new JButton("Pause");
	private boolean paused = false;
	
	private Simulation simulation;
	
	private MouseWheelListener wheelListener = new MouseWheelListener() {
		
		private int relative = 0;
		private int sensitivity = 2;
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			relative += e.getWheelRotation();
			
			while(relative >= sensitivity) {
				down();
				relative -= sensitivity;
			}
			
			while(relative <= -sensitivity) {
				up();
				relative += sensitivity;
			}
		}
		
		private void down() {
			timeFactorSlider.setValue(Math.max(timeFactorSlider.getMinimum(), timeFactorSlider.getValue()-10));
		}
		
		private void up() {
			timeFactorSlider.setValue(Math.min(timeFactorSlider.getMaximum(), timeFactorSlider.getValue()+10));
		}
	};
	
	public ProgressController(Simulation simulation) {
		super();
		
		this.simulation = simulation;
		
		createComponents();
		createLayout();
		
		setSelectedTimeFactor();
	}
	
	public MouseWheelListener getMouseWheelListener() {
		return wheelListener;
	}
	
	private double getSelectedTimeFactor() {
		return Math.pow(10, -(double)timeFactorSlider.getValue() / 100);
	}
	
	private void setSelectedTimeFactor() {
		double timeFactor = getSelectedTimeFactor();
		timeFactorNumber.setText(String.valueOf(timeFactor));
		simulation.setTimeFactor(timeFactor);
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
		
		timeFactorSlider.setValue(0);
		timeFactorSlider.setMinimum(0);
		timeFactorSlider.setMaximum(1000);
		
		timeFactorSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				setSelectedTimeFactor();
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
						.addComponent(timeFactorSlider)
						.addComponent(timeFactorNumber)
						)
				.addComponent(pauseButton)
				);
		
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(timeFactorLabel)
						.addComponent(timeFactorSlider)
						.addComponent(timeFactorNumber)
						)
				.addComponent(pauseButton)
				);
	}
}
