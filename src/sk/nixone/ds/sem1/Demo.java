package sk.nixone.ds.sem1;

import java.awt.Dimension;
import java.awt.Label;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import sk.nixone.ds.core.NthResultObserver;
import sk.nixone.ds.core.Randoms;
import sk.nixone.ds.core.StaticSimulation;

public class Demo extends JFrame
{
	public static void main(String[] args)
	{
		new Demo().setVisible(true);
	}
	
	private List<PercentageProgress> progresses = new LinkedList<PercentageProgress>();
	private XYSeriesCollection allSeries = new XYSeriesCollection();
	
	private Label allNameLabel = new Label("All dead:");
	private Label allPercentageLabel = new Label();
	private Label [] playerNameLabels = new Label[Game.PLAYER_COUNT];
	private Label [] playerPercentageLabels = new Label[Game.PLAYER_COUNT];
	private StaticSimulation<boolean[]> simulation = null;
	private Game game;
	
	public Demo() {
		super("Demo");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		game = new Game(new Randoms())
		{
			@Override
			protected int selectShootie(int shooter)
			{
				return getRandomShootieFor(shooter);
			}
		};
		simulation = new StaticSimulation<boolean[]>();
		
		simulation.addObserver(new NthResultObserver<boolean[]>(10000)
		{
			@Override
			public void onNthResult(int realCountOfResults, boolean[] result)
			{
				for(PercentageProgress progress : progresses) {
					progress.update(realCountOfResults);
				}
			}
		});
		
		createWiring();
		createComponents();
		
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				simulation.setThrower(new GameThrower(game));
				simulation.run(5000000);
			}
			
		}).start();
	}
	
	private void createComponents() {
		JFreeChart chart = ChartFactory.createXYLineChart("Semestral work 1", "Replications", "Probability", allSeries);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 300));
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(chartPanel)
				.addGroup(layout.createParallelGroup()
						.addComponent(allNameLabel)
						.addComponent(allPercentageLabel)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(playerNameLabels[0])
						.addComponent(playerPercentageLabels[0])
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(playerNameLabels[1])
						.addComponent(playerPercentageLabels[1])
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(playerNameLabels[2])
						.addComponent(playerPercentageLabels[2])
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(playerNameLabels[3])
						.addComponent(playerPercentageLabels[3])
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(playerNameLabels[4])
						.addComponent(playerPercentageLabels[4])
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(playerNameLabels[5])
						.addComponent(playerPercentageLabels[5])
						)
		);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(chartPanel)
				.addGroup(layout.createSequentialGroup()
						.addComponent(allNameLabel)
						.addComponent(allPercentageLabel)
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(playerNameLabels[0])
						.addComponent(playerPercentageLabels[0])
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(playerNameLabels[1])
						.addComponent(playerPercentageLabels[1])
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(playerNameLabels[2])
						.addComponent(playerPercentageLabels[2])
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(playerNameLabels[3])
						.addComponent(playerPercentageLabels[3])
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(playerNameLabels[4])
						.addComponent(playerPercentageLabels[4])
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(playerNameLabels[5])
						.addComponent(playerPercentageLabels[5])
						)
				);
		
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
	}
	
	private void createWiring() {
		AllDeadObserver allDeadObserver = new AllDeadObserver();
		XYSeries series = new XYSeries("All dead");
		allSeries.addSeries(series);
		
		progresses.add(new PercentageProgress(
				allPercentageLabel,
				allDeadObserver,
				series
		));
		
		simulation.addObserver(allDeadObserver);
		
		for(int p=0; p<Game.PLAYER_COUNT; p++) {
			String playerName = (char)((char)p+'A')+" stayed";
			series = new XYSeries(playerName);
			allSeries.addSeries(series);
			
			playerPercentageLabels[p] = new Label("");
			playerNameLabels[p] = new Label(playerName+":");
			
			PlayerStayedAliveObserver observer = new PlayerStayedAliveObserver(p);
			
			progresses.add(new PercentageProgress(
					playerPercentageLabels[p], 
					observer, 
					series
			));
			
			simulation.addObserver(observer);
		}
	}
}
