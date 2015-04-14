package gui;

import java.util.List;

import agenti.AgentJedalne;
import agenti.AgentKuchyne;
import entity.SkupinaZakaznikov;

public class ZakazniciTableModel extends MyTableModel
{
	final int sysId = 0;
	final int pocet = 2;
	final int stav = 1;
	final int casCakania = 3;
	final int stol = 5;
	final int cisloStola = 4;
	private List< SkupinaZakaznikov > _zakaznici;
	
	public ZakazniciTableModel(AgentJedalne agentJedalne, AgentKuchyne agentKuchyne)
	{
		super(agentJedalne, agentKuchyne);
		_zakaznici = null;
	}

	@Override
	public int getColumnCount()
	{
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch (columnIndex)
		{
		case sysId:
			return "Sys.id";
		case pocet:
			return "Počet";
		case stav:
			return "Stav";
		case casCakania:
			return "Čas čakania";
		case stol:
			return "Stôl";
		case cisloStola:
			return "Číslo stola";
		}
		return null;
	}

	@Override
	public int getRowCount()
	{
		int count = 0;
		try
		{
			_zakaznici = zakaznici();
			count = _zakaznici.size();
		}
		finally
		{
			return count;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		try
		{
			SkupinaZakaznikov zakaznici = _zakaznici.get(rowIndex);
			if (rowIndex < _zakaznici.size())
			switch (colIndex)
			{
			case sysId:
				return zakaznici.id();
			case pocet:
				return zakaznici.pocet();
			case stav:
				return zakaznici.stav();
			case casCakania:
				double casCakania = zakaznici.casCakania();
				if (zakaznici.cakaju() && zakaznici.mySim().isRunning())
				{
					casCakania += _agentJedalne.mySim().currentTime() - zakaznici.casZaciatkuCakania();
				}
				return MainWindow.formatTime(casCakania);
			case stol:
				return zakaznici.stol().pocetMiest() + " miestny";
			case cisloStola:
				return zakaznici.stol().cislo();
			}
		}
		finally { ; }
		return "0";
	}
	
	private List< SkupinaZakaznikov > zakaznici()
	{
		List< SkupinaZakaznikov > zakaznici = _agentJedalne.zakaznici();
		zakaznici.addAll(_agentKuchyne.zakaznici());
		return zakaznici;
	}
}
