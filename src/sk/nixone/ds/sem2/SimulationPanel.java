package sk.nixone.ds.sem2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
		
		createComponents();
		createLayout();
	}
	
	private void createComponents() {
		final DocumentListener listener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				anyChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				anyChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				anyChange();
			}
			
			private void anyChange() {
				try {
					simulation.setEstimatedCapacity(NumberUtil.readBig(capacityField.getText()));
					simulation.setBeforeLimit(NumberUtil.readBig(capacityBeforeField.getText()));
					simulation.setAfterLimit(NumberUtil.readBig(capacityAfterField.getText()));
				} catch(Throwable anyCause) {
					// do nothing
				}
			}
		};
		
		capacityField.getDocument().addDocumentListener(listener);
		capacityBeforeField.getDocument().addDocumentListener(listener);
		capacityAfterField.getDocument().addDocumentListener(listener);
		
		listener.removeUpdate(null);
		
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
