package sk.nixone.ds.agent.sem3.ui;

import sk.nixone.ds.agent.sem3.Simulation;
import sk.nixone.ds.agent.sem3.model.Line;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.ui.PercentageStatisticPanel;
import sk.nixone.ds.core.ui.StatisticPanel;
import sk.nixone.ds.core.ui.TimeStatisticPanel;

public class SimulationFrame extends SimulationFrameBase {
	
	public SimulationFrame(Simulation simulation, Model model, StationsLayout stationLayout) {
		super(simulation);
		StatisticPanel statisticPanel;
		
		VisualisationPanel visualPanel = new VisualisationPanel(model, stationLayout);
		simulation.getRefreshInvoked().add(visualPanel.getRefresher());
		addTab("Visual", visualPanel);
		
		ConfigPanel configPanel = new ConfigPanel(model);
		addTab("Config", configPanel);
		
		statisticPanel = new PercentageStatisticPanel(simulation.getLatePeopleStatistic(), "Late people");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("% Late", statisticPanel);
		
		statisticPanel = new TimeStatisticPanel(simulation.getPersonWaitingTimeStatistic(), "Person waiting time for bus");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Wait T", statisticPanel);
		
		statisticPanel = new StatisticPanel(simulation.getGainedStatistic(), "Gained money by private contractor");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Gain", statisticPanel);
		
		statisticPanel = new PercentageStatisticPanel(simulation.getBusFullnessStatistic(), "Bus fullness when exiting");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Full", statisticPanel);
		
		for(Line line : model.getLines()) {
			statisticPanel = new PercentageStatisticPanel(simulation.getLineLateStatistic(line), "Late people on "+line.getName());
			simulation.getReplicationEnded().add(statisticPanel);
			addTab("% "+line.getName(), statisticPanel);
		}
	}

}
