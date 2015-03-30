package sk.nixone.ds.core.ui;

import javax.swing.JLabel;

import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.Statistic;
import sk.nixone.ds.core.time.Simulation;

public class TimeStatisticPanel extends StatisticPanel {

	public TimeStatisticPanel(Simulation simulation, Statistic statistic,
			String dataName) {
		super(simulation, statistic, dataName);
	}
	
	@Override
	public Emitter<Double> createEmitter(JLabel label) {
		return new TimeLabelEmitter(label);
	}
}
