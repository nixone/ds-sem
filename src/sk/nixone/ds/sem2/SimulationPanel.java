package sk.nixone.ds.sem2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.NumberUtil;
import sk.nixone.ds.core.ui.SwingEmitter;

public class SimulationPanel extends JPanel {

	private JLabel capacityLabel = new JLabel("Capacity:");
	private JTextField capacityField = new JTextField("5300");
	private JLabel capacityBeforeLabel = new JLabel("Capacity before RTG:");
	private JTextField capacityBeforeField = new JTextField("4");
	private JLabel capacityAfterLabel = new JLabel("Capacity after RTG:");
	private JTextField capacityAfterField = new JTextField("5");
	private SimulationCanvas canvas;
	
	private Simulation simulation;
	
	public SimulationPanel(final Simulation simulation) {
		super();
		canvas = new SimulationCanvas(simulation);
		this.simulation = simulation;
		
		createLayout();
	}
	
	private void createComponents() {
		final ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.setEstimatedCapacity(NumberUtil.readBig(capacityField.getText()));
				simulation.setBeforeLimit(NumberUtil.readBig(capacityBeforeField.getText()));
				simulation.setAfterLimit(NumberUtil.readBig(capacityAfterField.getText()));
			}
		};
		
		capacityField.addActionListener(listener);
		capacityBeforeField.addActionListener(listener);
		capacityAfterField.addActionListener(listener);
		
		listener.actionPerformed(null);
		
		simulation.getStarted().add(new SwingEmitter<Object>(new Emitter<Object>() {
			
			@Override
			public void reset() {
				// nothing
			}
			
			@Override
			public void emit(Object value) {
				capacityField.setEnabled(false);
				capacityBeforeField.setEnabled(false);
				capacityAfterField.setEnabled(false);
			}
		}));
		
		simulation.getEnded().add(new SwingEmitter<Object>(new Emitter<Object>() {
			@Override
			public void reset() {
				// nothing
			}
			
			@Override
			public void emit(Object value) {
				capacityField.setEnabled(true);
				capacityBeforeField.setEnabled(true);
				capacityAfterField.setEnabled(true);
			}
		}));
	}
	
	private void createLayout() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(capacityLabel)
						.addComponent(capacityField)
						.addComponent(capacityBeforeLabel)
						.addComponent(capacityBeforeField)
						.addComponent(capacityAfterLabel)
						.addComponent(capacityAfterField)
						)
				.addComponent(canvas)
				);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
				.addGroup(layout.createSequentialGroup()
						.addComponent(capacityLabel)
						.addComponent(capacityField)
						.addComponent(capacityBeforeLabel)
						.addComponent(capacityBeforeField)
						.addComponent(capacityAfterLabel)
						.addComponent(capacityAfterField)
						)
				.addComponent(canvas)
				);
	}
}
