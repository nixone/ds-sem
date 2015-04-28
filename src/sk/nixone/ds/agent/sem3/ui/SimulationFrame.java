package sk.nixone.ds.agent.sem3.ui;

import sk.nixone.ds.agent.sem3.Simulation;
import sk.nixone.ds.agent.sem3.model.Model;
import sk.nixone.ds.core.ui.StatisticPanel;
import sk.nixone.ds.core.ui.TimeStatisticPanel;

public class SimulationFrame extends SimulationFrameBase {
	
	public SimulationFrame(Simulation simulation, Model model, StationsLayout stationLayout) {
		super(simulation);

		VisualisationPanel visualPanel = new VisualisationPanel(model, stationLayout);
		simulation.getRefreshInvoked().add(visualPanel.getRefresher());
		addTab("Visual", visualPanel);
		
		ConfigPanel configPanel = new ConfigPanel(model);
		addTab("Config", configPanel);
		
		StatisticPanel statisticPanel = new StatisticPanel(simulation.getLatePeopleStatistic(), "Late people ratio");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Late people", statisticPanel);
		
		statisticPanel = new TimeStatisticPanel(simulation.getPersonWaitingTimeStatistic(), "Person waiting time for bus");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Waiting time", statisticPanel);
		
		statisticPanel = new StatisticPanel(simulation.getGainedStatistic(), "Gained money by private contractor");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Gain", statisticPanel);
	}

}
