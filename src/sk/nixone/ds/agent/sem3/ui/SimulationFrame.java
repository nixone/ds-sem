package sk.nixone.ds.agent.sem3.ui;

import sk.nixone.ds.agent.sem3.Simulation;
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
		addTab("Visualisation", visualPanel);
		
		ConfigPanel configPanel = new ConfigPanel(model);
		addTab("Configugration", configPanel);
		
		statisticPanel = new PercentageStatisticPanel(simulation.getLatePeopleStatistic(), "Late people");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Late people", statisticPanel);
		
		statisticPanel = new TimeStatisticPanel(simulation.getPersonWaitingTimeStatistic(), "Person waiting time for bus");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Traveler waiting time", statisticPanel);
		
		statisticPanel = new StatisticPanel(simulation.getGainedStatistic(), "Gained money by private contractor");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Private transporter gain", statisticPanel);
		
		statisticPanel = new PercentageStatisticPanel(simulation.getBusFullnessStatistic(), "Bus fullness when exiting");
		simulation.getReplicationEnded().add(statisticPanel);
		addTab("Bus fullness", statisticPanel);
	}

}
