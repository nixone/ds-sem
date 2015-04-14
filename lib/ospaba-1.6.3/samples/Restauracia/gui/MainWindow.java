package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

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

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import simulacia.Config;
import simulacia.SimulaciaRestovracie;
import OSPABA.Simulation;
import OSPStat.Stat;

public class MainWindow implements ISimDelegate
{
	public static void main(String [] args)
	{
		new MainWindow();	
	}
	
	private final int kPorovnanieCasnikov = 5;
	private final int kOdporucanyPocetKucharov = 13;
	
	// sim
	private SimulaciaRestovracie _sim;
	private List< SimulaciaRestovracie > _simPorovnanieCasnikov = new ArrayList<>(kPorovnanieCasnikov);
	
	private boolean _isRunning = false;
	private boolean _isPaused = false;
	private boolean _fastMode = false;
	private boolean _endSim = false;
	
	private int _pocetReplikacii;
	private int _aktualnaReplikacia;

	private int _pocetCasnikov;
	private int _pocetKucharov;
	
	private Stat _casCakaniaStat;
	private Stat [] _casCakaniaPorovnanieStat = new Stat[kPorovnanieCasnikov];
	private Stat _pocetVolnychCasnikovStat = new Stat();
	private Stat _pocetVolnychKucharovStat = new Stat();
	private Stat _percentoNeobsluzenych = new Stat();
	private double [] _casPraceCasnik;
	private double [] _casPraceKuchar;
	
	// gui
	private JFrame _frame;
	
	private JButton _startButton = new JButton("▶");
	private JButton _pauseButton = new JButton("❙❙");
	
	private JLabel _simTimeLabel = new JLabel("11:00:00.00");
	private JProgressBar _replicationProgressBar = new JProgressBar();
	
	private JTextArea _pocetReplikaciiTextArea = new JTextArea("50", 1, 3);
	private JSlider _simSpeedSlider = new JSlider(1, 100, 100);
	private JSlider _simSpeedIntervalSlider = new JSlider(1, 5000, 5000);
	private JCheckBox _zrychlenyRezimCheckbox = new JCheckBox();
	
	private JCheckBox _porovnanieCasnikovCheckbox = new JCheckBox();

	private JTextArea _pocetCasnikovTextArea = new JTextArea("4", 1, 3);
	private JTextArea _pocetKucharovTextArea = new JTextArea("13", 1, 3);
	
	private JCheckBox _zahrievanieCheckbox = new JCheckBox();
	private JCheckBox _chladenieCheckbox = new JCheckBox();
	
	private JLabel _pocetZakaznikovVSysteme = new JLabel("0");
	private JLabel _pocetVygenerovanychZakaznikov = new JLabel("0");
	private JLabel _pocetObsluzenychZakaznikov = new JLabel("0 (0%)");
	private JLabel _pocetNeobsluzenychZakaznikov = new JLabel("0 (0%)");
	
	private JLabel _priemernyCasCakania = new JLabel("0");
	
	private JTable _tabZakaznici = new JTable();
	private JTable _tabStoli = new JTable();
	private JTable _tabCasnici = new JTable();
	private JTable _tabObjednavky = new JTable();
	private JTable _tabKuchari = new JTable();
	
	// chart
	private JFreeChart _chart;
	private XYSeriesCollection _dataset = new XYSeriesCollection();	
	private XYSeries  _series = new XYSeries(1);
	
	private JFreeChart _chartCasnici;
	private XYSeriesCollection _datasetCasnici = new XYSeriesCollection();	
	private XYSeries _seriesCasnici;
	
	public MainWindow()
	{
		initCharts();
		initComponents();
	}
	
	public void simulate()
	{
 		_replicationProgressBar.setMaximum((int)(_pocetReplikacii * Config.simEndTime));
 		
 		_casPraceCasnik = new double[_pocetCasnikov];
 		_casPraceKuchar = new double[_pocetKucharov];

		for (_aktualnaReplikacia=0; _aktualnaReplikacia <_pocetReplikacii; ++_aktualnaReplikacia)
		{
			Stat casCakania = new Stat();
			_sim = new SimulaciaRestovracie(0, _pocetCasnikov, _pocetKucharov, casCakania);
			
			_sim.onRun(sim -> onRun(sim));
			_sim.onStop(sim -> onStop(sim));
			_sim.onRefreshUI(sim -> refresh(sim));

			changeSimSpeed();
			_sim.agentOkolia().zacniPlanovanieZakaznikov();
			_sim.simulate(Config.simEndTime);
			
			_pocetVolnychCasnikovStat.addSample(_sim.agentJedalne().pocetVolnychCasnikovStat().mean());
			_pocetVolnychKucharovStat.addSample(_sim.agentKuchyne().pocetVolnychKucharovStat().mean());
			
			double neobsluzeny = _sim.agentOkolia().pocetNeobsluzenychZakaznikov();
			double vygenerovany = _sim.agentOkolia().pocetVygenerovanychZakaznikov();
			_percentoNeobsluzenych.addSample(100*neobsluzeny/vygenerovany);

			_casCakaniaStat.addSample(casCakania.mean());
			
			for (int i = 0; i < _pocetCasnikov; ++i)
	 		{
				_casPraceCasnik[i] += _sim.agentJedalne().casnici().get(i).casPrace();
	 		}
			for (int i = 0; i < _pocetKucharov; ++i)
	 		{
				_casPraceKuchar[i] += _sim.agentKuchyne().kuchari().get(i).casPrace();
	 		}
			
			if (_zrychlenyRezimCheckbox.isSelected())
			{
				_replicationProgressBar.setValue((int)((_aktualnaReplikacia+1)*Config.simEndTime));
			}
			if (_endSim) break;
		}
		
		double [] intervalSpolahlivosti = _casCakaniaStat.confidenceInterval_90();

		String message = "\nPriemerný čas čakania: " + formatTime(_casCakaniaStat.mean()) //_casCakaniaSum / _casCakaniaWeight)
				+ "\nInterval spolahlivosti(90%): (" + formatTime(intervalSpolahlivosti[0]) +", "+ formatTime(intervalSpolahlivosti[1]) +")"
				+ "\nPercento neobslúžených: " + String.format("%.2f", _percentoNeobsluzenych.mean()) + "%"
				+ "\nPriemerný počet voľných čašníkov: " + String.format("%.2f", _pocetVolnychCasnikovStat.mean())
				+ "\nPriemerný počet voľných kuchárov: " + String.format("%.2f", _pocetVolnychKucharovStat.mean())
				+ "\n\nPracovnik   Nepracoval";
		
		double celkovyCas = _pocetReplikacii * Config.trvanieObsluhyVRestauracii;
		for (int i = 0; i < _pocetCasnikov; ++i)
 		{
			message += "\n  Čašník "+ (i+1) +"\t\t\t\t\t";
			message += String.format("%.2f", 100d *(celkovyCas - _casPraceCasnik[i]) / celkovyCas) + "%";
 		}
		for (int i = 0; i < _pocetKucharov; ++i)
 		{
			message += "\n  Kuchár "+ (i+1) +"\t\t\t\t\t";
			message += String.format("%.2f", 100d *(celkovyCas - _casPraceKuchar[i]) / celkovyCas) + "%";
 		}

		JOptionPane.showMessageDialog(_frame, message, "Výsledky simulácie", JOptionPane.PLAIN_MESSAGE);
		
		_pocetVolnychCasnikovStat.clear();
		_pocetVolnychKucharovStat.clear();
	}
	
	public void simulatePorovnanie()
	{
		_replicationProgressBar.setMaximum((int)(_pocetReplikacii * kPorovnanieCasnikov));

		simulacia:
		for (_aktualnaReplikacia = 0; _aktualnaReplikacia <_pocetReplikacii; ++_aktualnaReplikacia)
		{
			for (int i = 0; i < kPorovnanieCasnikov; ++i)
			{	
				Stat casCakania = new Stat();
				SimulaciaRestovracie sim = new SimulaciaRestovracie(i+1, i+1, kOdporucanyPocetKucharov, casCakania);
				sim.registerDelegate(this);
	
				sim.agentOkolia().zacniPlanovanieZakaznikov();	
				sim.simulate(Config.simEndTime);
	
				_casCakaniaPorovnanieStat[i].addSample(casCakania.mean());

				_replicationProgressBar.setValue((int)((_aktualnaReplikacia * kPorovnanieCasnikov) + i+1));

				if (_endSim) break simulacia;
			}
		}
		String message = "Priemerný čas čakania";
		for (int i = 0; i < kPorovnanieCasnikov; ++i)
		{
			message += "\n" + (i+1) + (i == 0 ? " čašník: " : " čašníci: ") + formatTime(_casCakaniaPorovnanieStat[i].mean());
		}
		JOptionPane.showMessageDialog(_frame, message, "Porovnanie čašníkov", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void onRun(Simulation sim)
	{
		_startButton.setText("◼");
		_isRunning = true;
		_isPaused = false;

		if (! _zrychlenyRezimCheckbox.isSelected() && ! _porovnanieCasnikovCheckbox.isSelected())
		{
			SwingUtilities.invokeLater(() ->
			{
				_tabZakaznici.setModel(new ZakazniciTableModel(_sim.agentJedalne(), _sim.agentKuchyne()));
				_tabStoli.setModel(new StolTableModel(_sim.agentJedalne()));
				_tabKuchari.setModel(new KucharyTableModel(_sim.agentKuchyne()));
				_tabCasnici.setModel(new CasnciTableModel(_sim.agentJedalne()));
				_tabObjednavky.setModel(new ObjednavkaTableModel(_sim.agentKuchyne()));
			});
		}
	}
	
	public void onStop(Simulation sim)
	{
		_isRunning = false;
		_startButton.setText("▶");
		_isPaused = false;
		_pauseButton.setText("❙❙");

		if (_porovnanieCasnikovCheckbox.isSelected())
		{
			if((int)(100*_aktualnaReplikacia/(double)_pocetReplikacii) != (int)(100*(_aktualnaReplikacia+1)/(double)_pocetReplikacii))
			{
				final int id = ((SimulaciaRestovracie)sim).id();
				SwingUtilities.invokeLater(() ->
				{
					if (_seriesCasnici.getItems().size() > id-1)
					{
						_seriesCasnici.remove(id-1);
					}
					_seriesCasnici.add(id, _casCakaniaPorovnanieStat[id-1].mean() / 60d);
				});
			}
		}
		else
		{
			if (_aktualnaReplikacia > _pocetReplikacii * .3 
				&& (int)(200*_aktualnaReplikacia/(double)_pocetReplikacii)
				!= (int)(200*(_aktualnaReplikacia+1)/(double)_pocetReplikacii))  // prekresli graf 140 krat nezavisle na pockt replikacii
			{
				_series.add(_aktualnaReplikacia, _casCakaniaStat.mean() / 60d);
			}
		}
	}

	@Override
	public void simStateChanged(Simulation sim, SimState state)
	{
		switch (state)
		{
		case running:
			onRun(sim);
		break;

		case stopped:
			onStop(sim);
		break;
		}
	}
	
	public void startButton()
	{
		if (! _isRunning) // start
		{
			try
			{
				readInputParameters();
				_casCakaniaStat = new Stat();
				_percentoNeobsluzenych.clear();

				_endSim = false;
	        	_isRunning = true;
				_startButton.setText("◼");
				
				_series.clear();
				_seriesCasnici.clear();

				if (_porovnanieCasnikovCheckbox.isSelected())
				{
					_simPorovnanieCasnikov.clear();
					
					for (int i = 0; i < kPorovnanieCasnikov; ++i)
					{	
						_casCakaniaPorovnanieStat[i] = new Stat();
					}
							
					new Thread()
					{
						public void run()
						{
							simulatePorovnanie();
						}
					}.start();	
				}
				else
				{
					new Thread()
					{
						public void run()
						{
							simulate();
						}
					}.start();
				}
			}
			catch (RuntimeException ex)
	        {
	        	JOptionPane.showMessageDialog(_frame, ex.getMessage(), "Chybný vstup", JOptionPane.ERROR_MESSAGE);
	        }
		}
		else // stop
		{
			_endSim = true;
			_isRunning = false;
			_startButton.setText("▶");
			_isPaused = false;
			_pauseButton.setText("❙❙");
			if (_sim != null)
			{
				_sim.stopSimulation();
			}
		}
	}
	
	public void pauseButton()
	{
		if (_isRunning)
		{
			if (!_isPaused) // pause
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
	
	@Override
	public void refresh(Simulation sim)
	{	
		if (! _fastMode && _sim.currentTime() > 0)
		{
			if (sim == _sim)
			{
				_simTimeLabel.setText(formatTime(_sim.currentTime() + 11 * 3600));
				_replicationProgressBar.setValue((int)((_aktualnaReplikacia)*Config.simEndTime + Math.max(0, _sim.currentTime())));
				
				int pocetVygenerovanych = _sim.agentOkolia().pocetVygenerovanychZakaznikov();
				int pocetObsluzenych = _sim.agentOkolia().pocetObsluzenychZakaznikov();
				int pocetNeobsluzenych = _sim.agentOkolia().pocetNeobsluzenychZakaznikov();
				String ob = String.format("%.2f", pocetVygenerovanych == 0 ? 0 :  100.0*pocetObsluzenych/pocetVygenerovanych);
				String nob = String.format("%.2f", pocetVygenerovanych == 0 ? 0 :  100.0*pocetNeobsluzenych/pocetVygenerovanych);
	
				_pocetObsluzenychZakaznikov.setText(pocetObsluzenych + " (" + ob + "%)");
				_pocetNeobsluzenychZakaznikov.setText(pocetNeobsluzenych + " (" + nob + "%)");
				_pocetVygenerovanychZakaznikov.setText(""+pocetVygenerovanych);
				_pocetZakaznikovVSysteme.setText(""+_sim.agentModelu().pocetZakaznikovVRestauracii());
					
				_tabZakaznici.invalidate();
				_tabZakaznici.repaint();
				_tabStoli.invalidate();
				_tabStoli.repaint();
				_tabKuchari.invalidate();
				_tabKuchari.repaint();
				_tabCasnici.invalidate();
				_tabCasnici.repaint();
				_tabObjednavky.invalidate();
				_tabObjednavky.repaint();
	
				double casCakaniaMean = (_casCakaniaStat.mean() * _casCakaniaStat.sampleSize() + _sim.agentOkolia().statCelkovyCasCakania().mean()) / (_casCakaniaStat.sampleSize() + 1);
				_priemernyCasCakania.setText(formatTime(casCakaniaMean));
				
				_frame.repaint();
			}
		}
	}

	private void initComponents()
	{
		Border borderTemplate = BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.PLAIN, 10), Color.gray);
		_pocetReplikaciiTextArea.setBorder(borderTemplate);
		_pocetCasnikovTextArea.setBorder(borderTemplate);
		_pocetKucharovTextArea.setBorder(borderTemplate);
		
		_replicationProgressBar.setPreferredSize(new Dimension(450, 20));
		_simSpeedSlider.setPreferredSize(new Dimension(150, 20));
		_simSpeedIntervalSlider.setPreferredSize(new Dimension(150, 20));
		
		_zahrievanieCheckbox.setSelected(true);
		_chladenieCheckbox.setSelected(true);
		
		_zahrievanieCheckbox.addActionListener((ActionEvent e) ->
		{
			if (_zahrievanieCheckbox.isSelected())
			{
				Config.casOdKtorehoZakazniciCakajuPredRestauraciou = Config.casOtvoreniaRestauracie + Config.zahrievanie;
			}
			else
			{
				Config.casOdKtorehoZakazniciCakajuPredRestauraciou = Config.casOtvoreniaRestauracie;				
			}
		});
		_chladenieCheckbox.addActionListener((ActionEvent e) ->
		{
			if (_chladenieCheckbox.isSelected())
			{
				Config.simEndTime = Config.casZatvoreniaRestauracie + Config.chladenie;
			}
			else
			{
				Config.simEndTime = Config.casZatvoreniaRestauracie;
			}
		});
		_zrychlenyRezimCheckbox.addActionListener((ActionEvent e) ->
		{
			changeSimSpeed();
		});
		
		ChangeListener changeListener = (ChangeEvent e) ->
		{
			changeSimSpeed();
		};
		_simSpeedSlider.addChangeListener(changeListener);
		_simSpeedIntervalSlider.addChangeListener(changeListener);
		
		_frame = new JFrame();
		
		int w = 28;
		_startButton.setPreferredSize(new Dimension(w, w));
		_pauseButton.setPreferredSize(new Dimension(w, w));
		
		// controll panel
		JPanel controllPanel = new JPanel(new BorderLayout());
		controllPanel.setBorder(new EmptyBorder(5, 5, 0, 15));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(_startButton);
		_startButton.addActionListener((ActionEvent e) ->
		{
			startButton();
		});
		
		buttonPanel.add(_pauseButton);
		_pauseButton.addActionListener((ActionEvent e) ->
		{
			pauseButton();
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
				addMyComp("Zrýchlený režim: ", _zrychlenyRezimCheckbox);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Počet replikácií:  ", _pocetReplikaciiTextArea);
				addMyComp("Počet čašníkov:   ", _pocetCasnikovTextArea);
				addMyComp("Počet kuchárov:   ", _pocetKucharovTextArea);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Počet zakaznikov v systeme:   ", _pocetZakaznikovVSysteme);
				addMyComp("Počet vygenerovanych zakaznikov:   ", _pocetVygenerovanychZakaznikov);
				addMyComp("Počet obsluzenych zakaznikov:   ", _pocetObsluzenychZakaznikov);
				addMyComp("Počet neobsluzenych zakaznikov:   ", _pocetNeobsluzenychZakaznikov);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Simulačný čas:   ", _simTimeLabel);
				addMyComp("Priemerný čas čakania:   ", _priemernyCasCakania);
				addMyComp("Porovnanie čašníkov:   ", _porovnanieCasnikovCheckbox);
			}
		};
		new MyPanel(parameterPanel)
		{
			public void init()
			{
				addMyComp("Zahrievanie: ", _zahrievanieCheckbox);
				addMyComp("Chladenie: ", _chladenieCheckbox);
			}
		};
		
		// top panel = (control + parameter) panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(controllPanel);
		topPanel.add(parameterPanel);
		
		
		// table panel
        JTabbedPane tablePane = new JTabbedPane();
        addTable(tablePane, _tabZakaznici, "Zakaznici");
        addTable(tablePane, _tabCasnici, "Čašníci");
        addTable(tablePane, _tabKuchari, "Kuchári");
        addTable(tablePane, _tabStoli, "Stoly");
        addTable(tablePane, _tabObjednavky, "Objednávky");
        
        ChartPanel chartPanel = new ChartPanel(_chart);
        tablePane.addTab("Priemerný čas čakania", chartPanel);
       
        chartPanel = new ChartPanel(_chartCasnici);
        tablePane.addTab("Porovnanie čakania", chartPanel);
        

		// main panel = (top + chart) panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 4));
		mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(tablePane, BorderLayout.CENTER);


		_frame.add(mainPanel);

		_frame.setBackground(Color.WHITE);
		_frame.setSize(5000, 2000);
		_frame.setVisible(true);
		_frame.invalidate();
		_frame.validate();
	}
	
	public void initCharts()
	{
		_chart = ChartFactory.createXYLineChart("Priemerný čas čakania zákazníka", "Replikácia", "Priemerný čas čakania (min.)", _dataset, PlotOrientation.VERTICAL, false, true, false);
		_series = new XYSeries("w");
		_dataset.addSeries(_series);
	
		_chart.getPlot().setBackgroundPaint(Color.WHITE);
		((XYPlot)_chart.getPlot()).setRangeGridlinePaint(Color.BLACK);
		
		XYPlot plot = _chart.getXYPlot();
		plot.getDomainAxis().setAutoRange(true);
		((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		
		_chartCasnici = ChartFactory. createScatterPlot("Porovnanie priemerného času čakania zákazníkov", "Počet čašníkov", "Priemerný čas čakania (min.)", _datasetCasnici, PlotOrientation.VERTICAL, false, true, false);		
		_seriesCasnici = new XYSeries("Čašník");
		_datasetCasnici.addSeries(_seriesCasnici);
		
		_chartCasnici.getPlot().setBackgroundPaint(Color.WHITE);
		((XYPlot)_chartCasnici.getPlot()).setRangeGridlinePaint(Color.BLACK);

		plot = _chartCasnici.getXYPlot();
		plot.getDomainAxis().setAutoRange(true);
		((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
	}
	
	private void readInputParameters() throws InvalidParameterException
	{
		_pocetReplikacii = Integer.parseInt(_pocetReplikaciiTextArea.getText());
		_pocetCasnikov = Integer.parseInt(_pocetCasnikovTextArea.getText());
		_pocetKucharov = Integer.parseInt(_pocetKucharovTextArea.getText());

    	// kontrola vstupu
    	if (_pocetReplikacii <= 0) throw new InvalidParameterException("Počet replikácii musí byť celé, kladné číslo");
    	if (_pocetCasnikov <= 0) throw new InvalidParameterException("Počet čašníkov musí byť kladné reálne číslo");
    	if (_pocetKucharov <= 0) throw new InvalidParameterException("Počet kuchárov musí byť celé, kladné číslo");	
	}
	
	public void changeSimSpeed()
	{
		double speedMax = _simSpeedSlider.getMaximum() * .1;
		double speedValue = _simSpeedSlider.getValue() * .1;
		double intervalValue = _simSpeedIntervalSlider.getValue();

		if (_sim != null)
		{
			if (! _zrychlenyRezimCheckbox.isSelected())
			{
				_sim.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
			}
			else
			{
				_sim.setMaxSimSpeed();
			}
		}
	}
	
	/**
	 * @param time cas v sekundach
	 */
	static String formatTime(double time)
	{
		if (Double.isNaN(time)) time = .0d;
		int h = (int)Math.floor(time / 3600);
		int m = (int)Math.floor(time / 60) % 60;
		double s = ((int)time % 60) + time-(int)time;
		
		String ret = "";
		if (0 < h) ret += (h < 10 ? "0":"") + String.format("%d:", h);
		ret += (m < 10 ? "0":"") + String.format("%d:", m);
		ret += (s < 10 ? "0":"") + String.format("%.2f", s);
		return ret;
	}

	private void addTable(JTabbedPane tabPane, JTable tab, String name)
	{
		JScrollPane scrollPane = new JScrollPane(tab);
		JPanel pan = new JPanel(new BorderLayout());
        pan.add(scrollPane, BorderLayout.CENTER);
        tabPane.addTab(name, pan);
	}
}
