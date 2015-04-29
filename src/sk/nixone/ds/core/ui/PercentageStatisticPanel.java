package sk.nixone.ds.core.ui;

import javax.swing.JLabel;

import sk.nixone.ds.core.Emitter;
import sk.nixone.ds.core.SequenceStatistic;

public class PercentageStatisticPanel extends StatisticPanel {
	
	public PercentageStatisticPanel(SequenceStatistic statistic, String dataName) {
		super(statistic, dataName);
	}
	
	@Override
	public Emitter<Double> createEmitter(JLabel label) {
		return new PercentageLabelEmitter(label);
	}
}
