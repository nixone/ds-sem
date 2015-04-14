package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import dynamickyAgenti.AgentVozidla;
import exceptions.PrekrocenaKapacitaException;
import riadiaciAgenti.AgentDopravnikov;
import riadiaciAgenti.AgentModelu;
import riadiaciAgenti.AgentOceliarne;
import riadiaciAgenti.AgentOkolia;
import riadiaciAgenti.AgentSkladov;
import riadiaciAgenti.AgentVozidiel;
import riadiaciAgenti.AgentZeriavov;
import simulacia.Config;
import simulacia.Id;
import OSPABA.Agent;
import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import OSPStat.Stat;

@SuppressWarnings("serial")
public class GUI implements ISimDelegate
{
	private boolean _isRunning = false;
	private boolean _isPaused = false;
	private boolean _endSim = false;
	
	private JFrame _frame = new JFrame();;
	
	private JButton _startButton = new JButton("▶");
	private JButton _pauseButton = new JButton("❙❙");
	
	private JLabel _simTimeLabel = new JLabel("Deň 0 00:00:00.00");
	private JProgressBar _replicationProgressBar = new JProgressBar();
	
	private JLabel _neuspesneBehyLabel = new JLabel("0%");
	
	private JTextArea _pocetReplikaciiTextArea = new JTextArea("20", 1, 3);
	private JTextArea _pocetPracovnikovTextArea = new JTextArea("3", 1, 3);
	private JTextArea _pocetStarychVozidlielTextArea = new JTextArea("3", 1, 3);
	private JTextArea _pocetNovychVozidlielTextArea = new JTextArea("1", 1, 3);
	private JTextArea _vstupnyTokTextArea = new JTextArea("1.0", 1, 3);
	
	private JSlider _simSpeedSlider = new JSlider(1, 100, 100);
	private JSlider _simSpeedIntervalSlider = new JSlider(1, 5000, 5000);
	private JCheckBox _zrychlenyRezimCheckbox = new JCheckBox();
	
	private JTable _tabDopravniky = new JTable();
	private JTable _tabSklady = new JTable();
	private JTable _tabZeriavy = new JTable();
	private JTable _tabVozidla = new JTable();
	private JTable _tabTimy = new JTable();
	
	private JTable _tabRolkyVSkladoch = new JTable();
	private JTable _tabRolkyVDopravnikoch = new JTable();
	private JTable _tabRolkyVoVozidlach = new JTable();
	
	private JFreeChart _chartSklady;
	private XYSeriesCollection _datasetSklady = new XYSeriesCollection();	
	private XYSeries [] _seriesSklady = new XYSeries[Config.pocetSkladov];
		
	private JFreeChart _chartZeriavy;
	private XYSeriesCollection _datasetZeriavy = new XYSeriesCollection();	
	private XYSeries [] _seriesZeriavy = new XYSeries[Config.pocetZeriavov];
	
	private JFreeChart _chartDopravniky;
	private XYSeriesCollection _datasetDopravniky = new XYSeriesCollection();	
	private XYSeries [] _seriesDopravniky = new XYSeries[Config.pocetDopravnikov];
	
//	private JFreeChart _chartVozidla;
//	private XYSeriesCollection _datasetVozidla = new XYSeriesCollection();	
//	private XYSeries [] _seriesVozidla; // = new XYSeries[Config.pocetZeriavov];

	public GUI()
	{
		initCharts();

		int w = 28;
		_startButton.setPreferredSize(new Dimension(w, w));
		_pauseButton.setPreferredSize(new Dimension(w, w));
		
		_zrychlenyRezimCheckbox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				changeSimSpeed(sim());
			}
		});
		
		ChangeListener changeListener = new ChangeListener()
		{	
			public void stateChanged(ChangeEvent e)
			{
				changeSimSpeed(sim());
			}
		};
		_simSpeedSlider.addChangeListener(changeListener);
		_simSpeedIntervalSlider.addChangeListener(changeListener);
		
		Border borderTemplate = BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.PLAIN, 10), Color.gray);
		_pocetReplikaciiTextArea.setBorder(borderTemplate);
		_pocetPracovnikovTextArea.setBorder(borderTemplate);
		_pocetStarychVozidlielTextArea.setBorder(borderTemplate);
		_pocetNovychVozidlielTextArea.setBorder(borderTemplate);
		_vstupnyTokTextArea.setBorder(borderTemplate);
		
		// controll panel
		JPanel controllPanel = new JPanel(new BorderLayout());
		controllPanel.setBorder(new EmptyBorder(5, 5, 0, 15));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(_startButton);
		_startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ startButton(); }
		});
		
		buttonPanel.add(_pauseButton);
		_pauseButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ pauseButton(); }
		});
		controllPanel.add(buttonPanel, BorderLayout.WEST);
		controllPanel.add(_replicationProgressBar, BorderLayout.CENTER);
		
		// parameter panel
		JPanel parameterPanel = new JPanel();
		parameterPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Rýchlosť:", _simSpeedSlider);
				addMyComp("Interval:", _simSpeedIntervalSlider);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Počet replikácii: ", _pocetReplikaciiTextArea);
				addMyComp("Zrýchlený režim: ", _zrychlenyRezimCheckbox);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Počet starých vozidiel: ", _pocetStarychVozidlielTextArea);
				addMyComp("Počet nových vozidiel: ", _pocetNovychVozidlielTextArea);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Počet tímov: ", _pocetPracovnikovTextArea);
				addMyComp("Vstupný tok: ", _vstupnyTokTextArea);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Simulačný čas: ", _simTimeLabel);
			}
		};
		
		// top panel = (control + parameter) panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(controllPanel);
		topPanel.add(parameterPanel);
		
		// grid
		JPanel gridPanel = new JPanel(new GridLayout(2, 2));
		
		// tab panels
		JTabbedPane tablePane = new JTabbedPane();
		addTable(tablePane, _tabDopravniky, "Dopravníky");
		addTable(tablePane, _tabRolkyVDopravnikoch, "Rolky v dopravníkoch");
		ChartPanel chartPanel = new ChartPanel(_chartDopravniky);
        tablePane.addTab("Zaplnenie dopravníkov", chartPanel);
		gridPanel.add(tablePane);
		
		tablePane = new JTabbedPane();
		addTable(tablePane, _tabSklady, "Sklady");
		addTable(tablePane, _tabTimy, "Tímy");
		addTable(tablePane, _tabRolkyVSkladoch, "Rolky v skladoch");
		chartPanel = new ChartPanel(_chartSklady);
        tablePane.addTab("Zaplnenie skladov", chartPanel);
		gridPanel.add(tablePane);
		
		tablePane = new JTabbedPane();
		addTable(tablePane, _tabZeriavy, "Žeriavy");
		chartPanel = new ChartPanel(_chartZeriavy);
        tablePane.addTab("Vyťaženie žeriavov", chartPanel);
		gridPanel.add(tablePane);
		
		tablePane = new JTabbedPane();
		addTable(tablePane, _tabVozidla, "Vozidlá");
		addTable(tablePane, _tabRolkyVoVozidlach, "Rolky vo vozidlách");
		gridPanel.add(tablePane);
		
		// main panel = (top + table) panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 4));
		mainPanel.add(topPanel, BorderLayout.PAGE_START);
		mainPanel.add(gridPanel, BorderLayout.CENTER);

		_frame.add(mainPanel);
		_frame.setExtendedState(_frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		_frame.setVisible(true);
	}
	
	public void initCharts()
	{
		_chartSklady = ChartFactory.createXYLineChart("Priemerné zaplnenie skladov", "Čas [deň]", "Zaplnenie [%]", _datasetSklady, PlotOrientation.VERTICAL, true, true, false);
		
		for (int i = 0; i < _seriesSklady.length; ++i)
		{
			_seriesSklady[i] = new XYSeries(_sklady[i]);
			_datasetSklady.addSeries(_seriesSklady[i]);
		}
		_chartSklady.getPlot().setBackgroundPaint(Color.WHITE);
		((XYPlot)_chartSklady.getPlot()).setRangeGridlinePaint(Color.BLACK);
		XYPlot plot = _chartSklady.getXYPlot();
		plot.getDomainAxis().setAutoRange(true);
		((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		
		_chartZeriavy = ChartFactory.createXYLineChart("Priemerné vytaženie žeriavov", "Čas [deň]", "Vyťaženie [%]", _datasetZeriavy, PlotOrientation.VERTICAL, true, true, false);
		for (int i = 0; i < _seriesZeriavy.length; ++i)
		{
			_seriesZeriavy[i] = new XYSeries(_sklady[i]);
			_datasetZeriavy.addSeries(_seriesZeriavy[i]);
		}
		_chartZeriavy.getPlot().setBackgroundPaint(Color.WHITE);
		((XYPlot)_chartZeriavy.getPlot()).setRangeGridlinePaint(Color.BLACK);
		plot = _chartZeriavy.getXYPlot();
		plot.getDomainAxis().setAutoRange(true);
		((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		
		_chartDopravniky = ChartFactory.createXYLineChart("Priemerné zaplnenie dopravníkov", "Čas [deň]", "Vyťaženie [%]", _datasetDopravniky, PlotOrientation.VERTICAL, true, true, false);
		for (int i = 0; i < _seriesDopravniky.length; ++i)
		{
			_seriesDopravniky[i] = new XYSeries(_dopravniky[i]);
			_datasetDopravniky.addSeries(_seriesDopravniky[i]);
		}
		_chartDopravniky.getPlot().setBackgroundPaint(Color.WHITE);
		((XYPlot)_chartDopravniky.getPlot()).setRangeGridlinePaint(Color.BLACK);
		plot = _chartDopravniky.getXYPlot();
		plot.getDomainAxis().setAutoRange(true);
		((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
	}
	
	public void startButton()
	{
		if (! _isRunning) // start
		{
			try
			{
				readInputParameters();
			}
			catch (RuntimeException ex)
	        {
	        	JOptionPane.showMessageDialog(_frame, ex.getMessage(), "Chybný vstup", JOptionPane.ERROR_MESSAGE);
	        	return;
	        }
	
			_endSim = false;
	        _isRunning = true;
			_startButton.setText("◼︎︎");
			
			zmazGrafy();
			
			_replicationProgressBar.setMaximum(Config.pocetReplikacii);
			_replicationProgressBar.setValue(0);
			
			new Thread()
			{
				public void run()
				{
					simulate(Config.pocetReplikacii);
				}
			}.start();
		}
		else // stop
		{
			_endSim = true;
			_isRunning = false;
			_startButton.setText("▶︎");
			_isPaused = false;
			_pauseButton.setText("❙❙");
			if (_sim != null && _sim != null)
			{
				_sim.stopSimulation();
			}
		}
	}
	
	public void pauseButton()
	{
		if (_isRunning)
		{
			if (! _isPaused) // pause
			{
				_isPaused = true;
				_pauseButton.setText("▸▸");
				_sim.pauseSimulation();
			}
			else // resume
			{
				_isPaused = false;
				_pauseButton.setText("❙❙");

				_sim.resumeSimulation();
			}
		}
	}
	
	private void readInputParameters()
	{
		Config.pocetReplikacii = Integer.parseInt(_pocetReplikaciiTextArea.getText());
		Config.pocetTimov = Integer.parseInt(_pocetPracovnikovTextArea.getText());
		Config.pocetNovychVozidiel = Integer.parseInt(_pocetNovychVozidlielTextArea.getText());
		Config.pocetStarychVozidiel = Integer.parseInt(_pocetStarychVozidlielTextArea.getText());
		Config.intenzitaVstupnehoToku = Double.parseDouble(_vstupnyTokTextArea.getText());
	}

	public static void main(String[] args)
	{
		new GUI();
	}

	@Override
	public void refresh(Simulation sim)
	{
		if (! _zrychlenyRezimCheckbox.isSelected() && sim.currentTime() > 0
			&& Config.trvanieZahrievania < sim.currentTime())
		{
				_simTimeLabel.setText(formatTime(sim.currentTime()));
//				_replicationProgressBar.setValue((int)((_aktualnaReplikacia)*Config.simEndTime + Math.max(0, _sim.currentTime())));

				_tabDopravniky.invalidate();
				_tabSklady.invalidate();
				_tabZeriavy.invalidate();
				_tabVozidla.invalidate();
				_tabTimy.invalidate();
				_tabRolkyVSkladoch.invalidate();
				_tabRolkyVDopravnikoch.invalidate();
				_tabRolkyVoVozidlach.invalidate();
				
				_tabDopravniky.repaint();
				_tabSklady.repaint();
				_tabZeriavy.repaint();
				_tabVozidla.repaint();
				_tabTimy.revalidate();
				_tabRolkyVSkladoch.revalidate();
				_tabRolkyVDopravnikoch.revalidate();
				_tabRolkyVoVozidlach.revalidate();
	
				_frame.repaint();
				
				aktualizujGrafy(sim);
		}
	}

	@Override
	public void simStateChanged(final Simulation sim, SimState state)
	{
		switch (state)
		{
		case running:
			_startButton.setText("◼︎︎");
			_isRunning = true;
			_isPaused = false;
			if (0 < _aktualnaReplikacia)
				_neuspesneBehyLabel.setText(String.format("%.2f", 100d * _pocetNeuspesnychBehov / (_aktualnaReplikacia))+"%");
			
			_replicationProgressBar.setValue(_aktualnaReplikacia);

			if (! _zrychlenyRezimCheckbox.isSelected())
			{
				_simTimeLabel.setText("Zahrievanie...");
				
				SwingUtilities.invokeLater(new Runnable(){
				    public void run()
				    {
				    	_tabDopravniky.setModel(new DopravnikyTableModel(sim));
				    	_tabSklady.setModel(new SkladyTableModel(sim));
				    	_tabZeriavy.setModel(new ZeriavyTableModel(sim));
				    	_tabVozidla.setModel(new VozidlaTableModel(sim));
				    	_tabTimy.setModel(new TimyTableModel(sim));

				    	_tabRolkyVSkladoch.setModel(new RolkaTableModel((AgentSkladov)sim.findAgent(Id.agentSkladov)));
				    	_tabRolkyVDopravnikoch.setModel(new RolkaTableModel((AgentDopravnikov)sim.findAgent(Id.agentDopravnikov)));
				    	_tabRolkyVoVozidlach.setModel(new RolkaTableModel((AgentVozidiel)sim.findAgent(Id.agentVozidiel)));
				    }
				});
			}
		break;

		case stopped:
			_isRunning = false;
			_startButton.setText("▶︎");
			_isPaused = false;
			_pauseButton.setText("❙❙");

			_neuspesneBehyLabel.setText(String.format("%.2f", 100d * _pocetNeuspesnychBehov / (_aktualnaReplikacia+1))+"%");

			aktualizujGrafy(sim);
			
		break;

		default:
		}
	}

	private void addTable(JTabbedPane tabPane, JTable tab, String name)
	{
		JScrollPane scrollPane = new JScrollPane(tab);
		JPanel pan = new JPanel(new BorderLayout());
        pan.add(scrollPane, BorderLayout.CENTER);
        tabPane.addTab(name, pan);
	}
	
	public void changeSimSpeed(Simulation sim)
	{
		double speedMax = _simSpeedSlider.getMaximum() * .1;
		double speedValue = _simSpeedSlider.getValue() * .1;
		double intervalValue = _simSpeedIntervalSlider.getValue();

		if (sim != null)
		{
			if (! _zrychlenyRezimCheckbox.isSelected())
			{
				sim.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
			}
			else
			{
				sim.setMaxSimSpeed();
			}
		}
	}
	
	/**
	 * @param time cas v minutach
	 */
	static String formatTime(double time)
	{
		time -= Config.trvanieZahrievania;
		time *= 60;
		if (Double.isNaN(time)) time = .0d;
		int d = (int)Math.floor(time / 86400);
		int h = (int)Math.floor(time / 3600) % 24;
		int m = (int)Math.floor(time / 60) % 60;
		double s = ((int)time % 60) + time-(int)time;
		
		String ret = "";
		ret += String.format("Deň %d ", d);
		ret += (h < 10 ? "0":"") + String.format("%d:", h);
		ret += (m < 10 ? "0":"") + String.format("%d:", m);
		ret += (s < 10 ? "0":"") + String.format("%.2f", s);
		return ret;
	}
	
	private Simulation _sim;
	private int _pocetNeuspesnychBehov;
	
	private int _aktualnaReplikacia;
	private int _pocetReplikacii;
	
	private Stat [] _statVytazeneZeriavov = new Stat[3];
	private Stat [] _statZaplnenieSkladov = new Stat[4];
	private Stat _statVytazeneVozidiel = new Stat();
	
	private final String [] _sklady = { "S1", "S2", "S3", "S4" };
	private final String [] _zeriavy = { "Z1", "Z2", "Z3" };
	private final String [] _dopravniky = { "D1", "D2", "D3" };
	
	public void simulate(int pocetReplikacii)
	{	
		_pocetReplikacii = pocetReplikacii;
		_pocetNeuspesnychBehov = 0;
		
		for (int i = 0; i < 3; ++i)
		{
			_statVytazeneZeriavov[ i ] = new Stat();
		}
		for (int i = 0; i < 4; ++i)
		{
			_statZaplnenieSkladov[ i ] = new Stat();
		}
		
		for (_aktualnaReplikacia = 0; _aktualnaReplikacia < pocetReplikacii; ++_aktualnaReplikacia)
		{			
			System.out.println("R" + _aktualnaReplikacia);
			try 
			{
				_sim = makeSim();
				_sim.registerDelegate(this);
				changeSimSpeed(_sim);
				
				((AgentModelu)_sim.findAgent(Id.agentModleu)).inicializaciaSimulacie();
				_sim.simulate(Config.trvanieReplikacie);	

				zberStatistik(_sim);
			}
			catch (PrekrocenaKapacitaException ex)
			{
				++_pocetNeuspesnychBehov;
				System.out.println(ex.getMessage());
			}
			
			_replicationProgressBar.setValue(_aktualnaReplikacia + 1);

			if (! _zrychlenyRezimCheckbox.isSelected())
				zmazGrafy();
			if (_endSim) break;
		}
		JOptionPane.showMessageDialog(_frame, zaverecnaSprava());
	}
	
	public Simulation sim()
	{ return _sim; }

	public int aktualnaReplikacia()
	{ return _aktualnaReplikacia; }
	
	public int pocetNeuspesnychBehov()
	{ return _pocetNeuspesnychBehov; }
	
	private Simulation makeSim()
	{
		Simulation sim = new Simulation();
		
		Agent agentModelu = new AgentModelu(Id.agentModleu, sim, null);
		new AgentOkolia(Id.agentOkolia, sim, agentModelu);
		
		Agent agentOceliarne = new AgentOceliarne(Id.agentOceliarne, sim, agentModelu);
		new AgentSkladov(Id.agentSkladov, sim, agentOceliarne, Config.pocetTimov);
		new AgentZeriavov(Id.agentZeriavov, sim, agentOceliarne);
		new AgentDopravnikov(Id.agentDopravnikov, sim, agentOceliarne);
		new AgentVozidiel(Id.agentVozidiel, sim, agentOceliarne, Config.pocetStarychVozidiel, Config.pocetNovychVozidiel);
		
		return sim;
	}
	
	int [] idZeriavov = { Id.zeriav1, Id.zeriav2, Id.zeriav3 };
	int [] idSkladov = { Id.sklad1, Id.sklad2, Id.sklad3, Id.sklad4 };
	int [] idDopravnikov = { Id.dopravnik1, Id.dopravnik2, Id.dopravnik3 };
	
	public void zberStatistik(Simulation sim)
	{
		AgentVozidiel agentVozidiel = (AgentVozidiel)sim.findAgent(Id.agentVozidiel);
		AgentZeriavov agentZeriavov = (AgentZeriavov)sim.findAgent(Id.agentZeriavov);
		AgentSkladov agentSkladov = (AgentSkladov)sim.findAgent(Id.agentSkladov);

		double sum = .0;
		for(AgentVozidla vozidlo : agentVozidiel.vozidla())
		{
			sum += vozidlo.casPrace() / sim.currentTime();
		}
		_statVytazeneVozidiel.addSample(sum / agentVozidiel.vozidla().size());
		
		for(int i = 0; i < agentZeriavov.zeriavy().size(); ++i)
		{
			_statVytazeneZeriavov[i].addSample(agentZeriavov.zeriav(idZeriavov[i]).casPrace() / sim.currentTime());
		}
		for(int i = 0; i < agentSkladov.sklady().size(); ++i)
		{
			_statZaplnenieSkladov[i].addSample(agentSkladov.sklad(idSkladov[i]).statistika().mean());
		}		
	}
	
	public void aktualizujGrafy(Simulation sim)
	{
		boolean zrychlenyRezim = _zrychlenyRezimCheckbox.isSelected();
		double aktualnyCas = zrychlenyRezim ? _aktualnaReplikacia : sim.currentTime();
		double celkovyCas = zrychlenyRezim ? _pocetReplikacii : Config.trvanieReplikacie;
		
		if ((int)(200*aktualnyCas/celkovyCas)
			!= (int)(200*(aktualnyCas+1)/celkovyCas))  // prekresli graf 200 krat nezavisle na trvani simulacneho behu
		{
			AgentZeriavov agentZeriavov = (AgentZeriavov)sim.findAgent(Id.agentZeriavov);
			AgentSkladov agentSkladov = (AgentSkladov)sim.findAgent(Id.agentSkladov);
			AgentDopravnikov agentDopravnikov = (AgentDopravnikov)sim.findAgent(Id.agentDopravnikov);
			
			if (! zrychlenyRezim) aktualnyCas /= 1440d;

			for (int i = 0; i < _seriesSklady.length; ++i)
			{
				_seriesSklady[i].add(aktualnyCas, agentSkladov.sklad(idSkladov[i]).statistika().mean() / (double)agentSkladov.sklad(idSkladov[i]).kapacita() * 100);
			}
			for (int i = 0; i < _seriesZeriavy.length; ++i)
			{
				_seriesZeriavy[i].add(aktualnyCas, agentZeriavov.zeriav(idZeriavov[i]).casPrace() / sim.currentTime() * 100);
			}
			for (int i = 0; i < _seriesDopravniky.length; ++i)
			{
				_seriesDopravniky[i].add(aktualnyCas, agentDopravnikov.dopravnik(idDopravnikov[i]).statistika().mean() / (double)agentDopravnikov.dopravnik(idDopravnikov[i]).kapacita() * 100);
			}
		}
	}
	
	private void zmazGrafy()
	{
		for (int i = 0; i < _seriesSklady.length; ++i)
		{
			_seriesSklady[i].clear();
		}
		for (int i = 0; i < _seriesZeriavy.length; ++i)
		{
			_seriesZeriavy[i].clear();
		}
		for (int i = 0; i < _seriesDopravniky.length; ++i)
		{
			_seriesDopravniky[i].clear();
		}
	}
	
	private String zaverecnaSprava()
	{
		double [] confidence = _statVytazeneVozidiel.confidenceInterval_95();
		String message = "Priemerné vytaženie vozidiel: " + String.format("%.2f", 100d * _statVytazeneVozidiel.mean()) + "%";
		message += "; interval sopľahlivosti(95%): (" + String.format("%.2f", 100d * confidence[0]) +", "+ String.format("%.2f", 100d * confidence[1]) + ")";
		for(int i = 0; i < _statVytazeneZeriavov.length; ++i)
		{
			confidence = _statVytazeneZeriavov[i].confidenceInterval_95();
			message += "\nPriemerné vytaženie žeriavu "+ _zeriavy[i] +": " + String.format("%.2f", 100d * _statVytazeneZeriavov[i].mean()) + "%";
			message += "; interval sopľahlivosti(95%): (" + String.format("%.2f", 100d * confidence[0]) +", "+ String.format("%.2f", 100d * confidence[1]) + ")";
		}
		for(int i = 0; i < _statZaplnenieSkladov.length; ++i)
		{
			confidence = _statZaplnenieSkladov[i].confidenceInterval_95();
			message += "\nPriemerné zaplnenie skladu "+ _sklady[i] +": " + String.format("%.2f", _statZaplnenieSkladov[i].mean());
			message += "; interval sopľahlivosti(95%): (" + String.format("%.2f", confidence[0]) +", "+ String.format("%.2f", confidence[1]) + ")";
		}	
		return message;
	}
}
