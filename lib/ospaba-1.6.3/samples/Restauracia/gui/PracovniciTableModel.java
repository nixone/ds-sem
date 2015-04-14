package gui;

import agenti.AgentJedalne;
import agenti.AgentKuchyne;
import entity.Pracovnik;
import simulacia.Config;

public abstract class PracovniciTableModel extends MyTableModel
{
	final int pracuje = 0;
	final int celkovyCasPrace = 1;
	final int nepracoval = 2;
	final int cisloStola = 3;

	public PracovniciTableModel(AgentJedalne agentJedalne, AgentKuchyne agentKuchyne)
	{
		super(agentJedalne, agentKuchyne);
	}

	@Override
	public int getColumnCount()
	{
		return 4;
	}

	@Override
	public String getColumnName(int colIndex)
	{
		switch (colIndex)
		{
		case pracuje:
			return "Pracuje";
		case celkovyCasPrace:
			return "Celkový čas práce";
		case nepracoval:
			return "Nepracoval";
		case cisloStola:
			return "Číslo stola";
		}
		return null;
	}

	public Object getValueFor(Pracovnik pracovnik, int colIndex)
	{
		double casPrace = pracovnik.casPrace();
		if (pracovnik.pracuje() && pracovnik.mySim().isRunning())
		{
			casPrace += pracovnik.mySim().currentTime() - pracovnik.casZaciatkuPrace();
		}
		switch (colIndex)
		{
		case pracuje:
			return pracovnik.pracuje() ? "Pracuje" : "Nepracuje";
		case celkovyCasPrace:
			return MainWindow.formatTime(casPrace);
		case nepracoval:
			double cas = pracovnik.mySim().isRunning() ? Math.min(pracovnik.mySim().currentTime(), Config.casZatvoreniaRestauracie) - casPrace : Config.casZatvoreniaRestauracie - casPrace;
			return MainWindow.formatTime(cas);
		case cisloStola:
			int cislo = pracovnik.cisloStola();
			return cislo != -1 ? cislo : "-";
		}
		return null;
	}
}
