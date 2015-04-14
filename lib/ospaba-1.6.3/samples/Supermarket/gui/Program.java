package supermarket.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Box;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import supermarket.simulacia.Mc;
import supermarket.simulacia.SimulaciaSupermarketu;
import supermarket.simulacia.Sprava;
import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import OSPStat.Stat;

public class Program implements ISimDelegate, ActionListener, ChangeListener
{	
	private Stat _casCakaniaStat = new Stat();
	private double _dlzkaFrontov;
	private double _trvanieReplikacie;
	
	private SimulaciaSupermarketu _sim;
	private int _currentReplication;

	private String _resultString;
	
	public static void main(String [] args)
	{
		new Program();
	}
	
	public Program()
	{	
		initComponents();
	}
	
	public void run()
	{
		_dlzkaFrontov = .0;
		
		_currentReplication = 0;
		_trvanieReplikacie = 54000d;
		int pocetReplikacii = 0;
		try
		{
			pocetReplikacii = Integer.parseInt(_pocetReplikaciiTextArea.getText());
		}
		catch (RuntimeException ex)
		{
			JOptionPane.showMessageDialog(_frame, "Pocet replikacii musi byt cislo");
		}
		_replicationProgressBar.setMaximum((int)(pocetReplikacii * _trvanieReplikacie));
		_replicationProgressBar.setValue(0);
		
		int pocetPracovnikovZelenina, pocetPracovnikovMasoSyr, pocetPracovnikovPokladne;
		try
		{
			pocetPracovnikovZelenina = Integer.parseInt(_pocetZeleninaTextArea.getText());
			pocetPracovnikovMasoSyr = Integer.parseInt(_pocetMasoTextArea.getText());
			pocetPracovnikovPokladne = Integer.parseInt(_pocetPokladnaTextArea.getText());
		}
		catch (RuntimeException ex)
		{
			JOptionPane.showMessageDialog(_frame, "Pocet pracovnikov musi byt cele cislo");
			return;
		}

		for (int i = 0; i < pocetReplikacii; ++i)
		{
			_currentReplication = i+1;
			_replicationLabel.setText("  (" + _currentReplication + "/" + pocetReplikacii + ")");
			
			_sim = new SimulaciaSupermarketu(pocetPracovnikovZelenina, pocetPracovnikovMasoSyr, pocetPracovnikovPokladne, _typSimulacieCheckbox.isSelected());
			_sim.registerDelegate(this);

			if (! _zrychlenyRezimCheckbox.isSelected())
			{
				_sim.setSimSpeed(simSpeed_interval(), simSpeed_duration());
			}
			
			Sprava msg = new Sprava(_sim, null);
			msg.setAddressee(_sim.agentModelu());
			msg.setCode(Mc.init);
			_sim.boss().manager().notice(msg);

			_sim.simulate(_trvanieReplikacie);
			
			_replicationProgressBar.setValue((int)((_currentReplication+1)*_trvanieReplikacie));
			if (endSim) break;
			
			_casCakaniaStat.addSample(_sim.agentModelu().statistikaCelkovyCasCakania().mean());
			_dlzkaFrontov += _sim.agentPokladne().statistikaDlzkaFrontu().mean(); // _sim.agentModelu().statistikaDlzkaFrontov().mean();
		}
		
		_resultString = "Priemerný čas čakania zákazníkov:  " + formatTime(_casCakaniaStat.mean()) +//_casCakania/pocetReplikacii) +
				"\nPriemerný počet zákazníkov pri pokladniach:  " + String.format("%.2f",_dlzkaFrontov/pocetReplikacii);
		
		JOptionPane.showMessageDialog(_frame, _resultString,"Výsledky simulácie", JOptionPane.PLAIN_MESSAGE);
		resultPanelPanel.setVisible(true);
	}

	@Override
	public void simStateChanged(Simulation sim, SimState state)
	{
		switch (state)
		{
		case running:
			_startButton.setText("Stop");
			resultPanelPanel.setVisible(false);
			
			if (! _zrychlenyRezimCheckbox.isSelected())
			{
				SwingUtilities.invokeLater(new Runnable(){
				    public void run()
				    {
						_tabZeleninaPracovnici.setModel(new PracovniciTableModel(_sim.agentNakupuZeleniny()));
						_tabMasoSyrPracovnici.setModel(new PracovniciTableModel(_sim.agentNakupuMasoSyr()));
						_tabPokladnaPracovnici.setModel(new PracovniciTableModel(_sim.agentPokladne()));
						
						_tabPokladna.setModel(new AgnetSPracovnikomTableModel(_sim.agentPokladne()));
						_tabZelenina.setModel(new AgnetSPracovnikomTableModel(_sim.agentNakupuZeleniny()));
						_tabMasoSyr.setModel(new AgnetSPracovnikomTableModel(_sim.agentNakupuMasoSyr()));
					
						_tabNakup.setModel(new ZoznamZakaznikovTableModel(_sim.agentNakupu().zakaznici()));
						_tabObsluzeny.setModel(new ZoznamZakaznikovTableModel(_sim.agentModelu().obsluzenyZakaznici()));
				    }
				});
			}
			break;

		case stopped:
			_startButton.setText("Start");

			break;
		default:
		}
	}
	
	/**
	 * Aktualizuje GUI
	 */
	@Override
	public void refresh(Simulation sim)
	{		
		_simTimeLabel.setText("Simulačný čas: " + formatTime(_sim.currentTime() + 7 * 3600));
		_replicationProgressBar.setValue((int)((_currentReplication-1)*_trvanieReplikacie + _sim.currentTime()));
	
		SwingUtilities.invokeLater(new Runnable(){
		    public void run()
		    {
				_simTimeLabel.setText("Simulačný čas: " + formatTime(_sim.currentTime() + 7 * 3600));
				_replicationProgressBar.setValue((int)((_currentReplication-1)*_trvanieReplikacie + _sim.currentTime()));
				
				_tabNakup.invalidate();
				_tabNakup.invalidate();
				_tabZelenina.invalidate();
				_tabMasoSyr.invalidate();
				_tabPokladna.invalidate();
				_tabZeleninaPracovnici.invalidate();
				_tabMasoSyrPracovnici.invalidate();
				_tabPokladnaPracovnici.invalidate();
				_tabObsluzeny.invalidate();
				
				_tabNakup.repaint();
				_tabZelenina.repaint();
				_tabMasoSyr.repaint();
				_tabPokladna.repaint();
				_tabZeleninaPracovnici.repaint();
				_tabMasoSyrPracovnici.repaint();
				_tabPokladnaPracovnici.repaint();
				_tabObsluzeny.repaint();
		
				_frame.repaint();
				
				DecimalFormat df = new DecimalFormat("0.00");
				_statPokladnaLabel.setText("  Priemerná dĺžka frontu: "+ df.format(_sim.agentPokladne().statistikaDlzkaFrontu().mean())
						+"         Priemerný čas čakania: "+ formatTime(_sim.agentPokladne().statistikaCasCakania().mean())
						+"         Počet pracovníkov: " + _sim.agentPokladne().pracovnici().size());
				_statPokladnaLabel2.setText(_statPokladnaLabel.getText());
				_statZeleninaLabel.setText("  Priemerná dĺžka frontu: "+ df.format(_sim.agentNakupuZeleniny().statistikaDlzkaFrontu().mean())
						+"         Priemerný čas čakania: "+ formatTime(_sim.agentNakupuZeleniny().statistikaCasCakania().mean())
						+"         Počet pracovníkov: " + _sim.agentNakupuZeleniny().pracovnici().size());
				_statZeleninaLabel2.setText(_statZeleninaLabel.getText());
				_statMasoSyrLabel.setText("  Priemerná dĺžka frontu: "+ df.format(_sim.agentNakupuMasoSyr().statistikaDlzkaFrontu().mean())
						+"         Priemerný čas čakania: "+ formatTime(_sim.agentNakupuMasoSyr().statistikaCasCakania().mean())
						+"         Počet pracovníkov: " + _sim.agentNakupuMasoSyr().pracovnici().size());
				_statMasoSyrLabel2.setText(_statMasoSyrLabel.getText());
				
				_pocetZakaznikovVSysteme.setText(""+_sim.pocetZakaznikovVSysteme());
				_pocetObsluzenychZakaznikov.setText(""+_sim.pocetObsluzenychZakaznikov());
				_pocetVygenerovanychZakaznikov.setText(""+_sim.pocetVygenerovanychZakaznikov());
		    }
		});
	}
	
	private JFrame _frame;

	private JButton _startButton = new JButton("Start");
	private JButton _pauseButton = new JButton("Pause");
	private JButton _resultButton = new JButton("Zobraz výsledky");
	
	private JSlider _simSpeedIntervalSlider = new JSlider(10, 5000, 5000);
	private JSlider _simSpeedDurationSlider = new JSlider(1, 20, 20);

	private JTable _tabPokladna = new JTable();
	private JTable _tabZelenina = new JTable();
	private JTable _tabMasoSyr = new JTable();
	private JTable _tabNakup = new JTable();
	private JTable _tabObsluzeny = new JTable();
	private JTable _tabZeleninaPracovnici = new JTable();
	private JTable _tabMasoSyrPracovnici = new JTable();
	private JTable _tabPokladnaPracovnici = new JTable();
	
	private JLabel _simTimeLabel = new JLabel("Simulačný čas: 07:00:00.00");
	private JLabel _replicationLabel = new JLabel("  (0/0)");
	private JProgressBar _replicationProgressBar = new JProgressBar();
	
	private JLabel _statPokladnaLabel = new JLabel("");
	private JLabel _statPokladnaLabel2 = new JLabel("");
	private JLabel _statZeleninaLabel = new JLabel("");
	private JLabel _statZeleninaLabel2 = new JLabel("");
	private JLabel _statMasoSyrLabel = new JLabel("");
	private JLabel _statMasoSyrLabel2 = new JLabel("");
	
	private JLabel _pocetZakaznikovVSysteme = new JLabel("0");
	private JLabel _pocetVygenerovanychZakaznikov = new JLabel("0");
	private JLabel _pocetObsluzenychZakaznikov = new JLabel("0");
	
	private JTextArea _pocetReplikaciiTextArea = new JTextArea("20", 1, 3);
	private JTextArea _pocetZeleninaTextArea = new JTextArea("8", 1, 3);
	private JTextArea _pocetMasoTextArea = new JTextArea("16", 1, 3);
	private JTextArea _pocetPokladnaTextArea = new JTextArea("120", 1, 3);
	
	private JPanel resultPanelPanel;

	private JCheckBox _typSimulacieCheckbox = new JCheckBox();
	private JCheckBox _zrychlenyRezimCheckbox = new JCheckBox();

	private boolean endSim = false;

	private void initComponents()
	{
		_frame = new JFrame();
		_frame.setVisible(true);
        _frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        _startButton.addActionListener(this);
        _pauseButton.addActionListener(this);
        _resultButton.addActionListener(this);
        
        _simSpeedIntervalSlider.addChangeListener(this);
        _simSpeedDurationSlider.addChangeListener(this);
         
        JPanel pan;
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel topTopPanel = new JPanel();
        topTopPanel.setLayout(new BoxLayout(topTopPanel, BoxLayout.LINE_AXIS));
        topPanel.add(topTopPanel, BorderLayout.PAGE_START);

        topTopPanel.add(_startButton);
        topTopPanel.add(_pauseButton);
        topTopPanel.add(new JLabel("  "));
        topTopPanel.add(_replicationProgressBar); //_replicationLabel);
        topTopPanel.add(_replicationLabel);
        topTopPanel.add(Box.createHorizontalGlue());
        topTopPanel.add(_simTimeLabel);
        topTopPanel.setBorder(new EmptyBorder(5, 5, 0, 15));
        
        
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        JPanel parameterListPanel = new JPanel();
        parameterListPanel.setLayout(new BoxLayout(parameterListPanel, BoxLayout.Y_AXIS));
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        JPanel countPanel = new JPanel();
        countPanel.setLayout(new BoxLayout(countPanel, BoxLayout.Y_AXIS));
        
        resultPanelPanel = new JPanel();
        resultPanelPanel.add(_resultButton);
        resultPanelPanel.setVisible(false);
        
        JPanel topBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        topPanel.add(topBottomPanel, BorderLayout.CENTER);
        topBottomPanel.add(sliderPanel);
        topBottomPanel.add(parameterListPanel);
        topBottomPanel.add(checkboxPanel);
        topBottomPanel.add(countPanel);
        topBottomPanel.add(resultPanelPanel);
        
          
        pan = new JPanel(new FlowLayout(FlowLayout.LEADING));
        pan.add(new JLabel("Počet replikácií:  "));
        pan.add(_pocetReplikaciiTextArea);
        pan.setBorder(new EmptyBorder(5,15,5,0));
        sliderPanel.add(pan);

        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Rýchlosť"), BorderLayout.LINE_START);
        pan.add(_simSpeedDurationSlider, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(0,20,0,0));
        sliderPanel.add(pan);
        
        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Interval"), BorderLayout.LINE_START);
        pan.add(_simSpeedIntervalSlider, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(0,20,0,0));
        sliderPanel.add(pan);

        
        int vmargin = 7;
        int hmargin = 22;
        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Počet ovocinárov:"), BorderLayout.LINE_START);
        pan.add(_pocetZeleninaTextArea, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        parameterListPanel.add(pan);
        
        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Počet mäsiarov:  "), BorderLayout.LINE_START);
        pan.add(_pocetMasoTextArea, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        parameterListPanel.add(pan);

        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Počet pokladníkov:  "), BorderLayout.LINE_START);
        pan.add(_pocetPokladnaTextArea, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        parameterListPanel.add(pan);

        
        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Nový spôsob práce:  "), BorderLayout.LINE_START);
        pan.add(_typSimulacieCheckbox, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        checkboxPanel.add(pan);
        
        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Zrýchlený režim:  "), BorderLayout.LINE_START);
        pan.add(_zrychlenyRezimCheckbox, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        checkboxPanel.add(pan);


        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Počet zákazníkov v systéme:  "), BorderLayout.LINE_START);
        pan.add(_pocetZakaznikovVSysteme, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        countPanel.add(pan);
        
        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Pocet vygenerovaných zákazníkov:  "), BorderLayout.LINE_START);
        pan.add(_pocetVygenerovanychZakaznikov, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        countPanel.add(pan);
        
        pan = new JPanel(new BorderLayout());
        pan.add(new JLabel("Pocet obsluzených zakaznikov:   "), BorderLayout.LINE_START);
        pan.add(_pocetObsluzenychZakaznikov, BorderLayout.LINE_END);
        pan.setBorder(new EmptyBorder(vmargin,hmargin,vmargin,hmargin));
        countPanel.add(pan);
        
        
        JScrollPane scrollPane = new JScrollPane(_tabNakup);
        pan = new JPanel(new BorderLayout());
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Nákup", pan);

        scrollPane = new JScrollPane(_tabZelenina);
        pan = new JPanel(new BorderLayout());
        pan.add(_statZeleninaLabel, BorderLayout.PAGE_START);
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Zelenina-zákazníci", pan);
        
        scrollPane = new JScrollPane(_tabZeleninaPracovnici);
        pan = new JPanel(new BorderLayout());
        pan.add(_statZeleninaLabel2, BorderLayout.PAGE_START);
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Zelenina-pracovníci", pan);
        
        scrollPane = new JScrollPane(_tabMasoSyr);
        pan = new JPanel(new BorderLayout());
        pan.add(_statMasoSyrLabel, BorderLayout.PAGE_START);
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("MäsoSyr-zákazníci", pan);
        
        scrollPane = new JScrollPane(_tabMasoSyrPracovnici);
        pan = new JPanel(new BorderLayout());
        pan.add(_statMasoSyrLabel2, BorderLayout.PAGE_START);
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("MäsoSyr-pracovníci", pan);

        scrollPane = new JScrollPane(_tabPokladna);
        pan = new JPanel(new BorderLayout());
        pan.add(_statPokladnaLabel, BorderLayout.PAGE_START);
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Pokladňa-zákazníci", pan);
        
        scrollPane = new JScrollPane(_tabPokladnaPracovnici);
        pan = new JPanel(new BorderLayout());
        pan.add(_statPokladnaLabel2, BorderLayout.PAGE_START);
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Pokladňa-pracovníci", pan);
        
        scrollPane = new JScrollPane(_tabObsluzeny);
        pan = new JPanel(new BorderLayout());
        pan.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Obslúžení", pan);
        
        
        _frame.add(mainPanel);
        _frame.pack();
        _frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    }
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==_startButton)
		{
			if (_sim != null && _sim.isRunning()) // stop sim
			{
				endSim = true;
				_sim.stopSimulation();	
			}
			else // start sim
			{
				_pauseButton.setText("Pause");
				endSim = false;
				final Program p = this;
				new Thread()
				{
					public void run()
					{
						p.run();
					}
				}.start();				
			}		
		}
		else if (e.getSource()==_pauseButton)
		{
			if (_sim.isRunning())
			{
				if (_sim.isPaused())
				{
					_pauseButton.setText("Pause");
					_sim.resumeSimulation();
				}
				else
				{
					_pauseButton.setText("Resume");
					_sim.pauseSimulation();
				}
			}
		}
		else if (e.getSource()==_resultButton)
		{
			JOptionPane.showMessageDialog(_frame, _resultString,"Výsledky simulácie", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource()==_simSpeedIntervalSlider||e.getSource()==_simSpeedDurationSlider)
		{
//		 	if (_sim != null && ! _zrychlenyRezimCheckbox.isSelected())
//			{
//				_sim.setSimSpeed(simSpeed_interval(), simSpeed_duration());
//			}
			changeSimSpeed();
		}
	}
	
	public void changeSimSpeed()
	{
		double speedMax = _simSpeedDurationSlider.getMaximum() * .1;
		double speedValue = _simSpeedDurationSlider.getValue() * .1;
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
	
	private double simSpeed_interval()
	{
		return _simSpeedIntervalSlider.getValue()*0.01;
	}
	
	private double simSpeed_duration()
	{
		return (_simSpeedDurationSlider.getMaximum() - _simSpeedDurationSlider.getValue())*0.02;
	}
	
	public static String formatTime(double time)
	{
		if (time == 0) return "0";
		
		DecimalFormat df_mh = new DecimalFormat("00");
		DecimalFormat df_s = new DecimalFormat("00.00");
		
		double h = (int)time / 3600;
		double m = ((int)time / 60) % 60;
		double s = ((int)time) % 60 + time - (int)time;
		
		return ((int)h == 0 ? "" : df_mh.format(h) + ":")
				+ ((int)h == 0 && (int)m == 0 ? "" : df_mh.format(m) + ":")
				+ df_s.format(s);
	}
}
