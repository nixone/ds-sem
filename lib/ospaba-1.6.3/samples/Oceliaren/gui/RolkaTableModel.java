package gui;

import entity.Rolka;

public class RolkaTableModel extends MyTableModel
{
	private RolkyDataSource _datasource;
	
	private final int id = 0;
	private final int jeOpracovana = 1;
	private final int jePripravenaNaExpedovanie = 2;
	private final int sklad = 3;
	private final int vozidlo = 4;
	private final int dopravnik = 5;
	private final int cielovySklad = 6;
	
	private final int _colCount = 7;

	public RolkaTableModel(RolkyDataSource datasource)
	{
		super(null);
		_datasource = datasource;
	}

	@Override
	public int getRowCount()
	{
		return _datasource.rolky().size();
	}

	@Override
	public int getColumnCount()
	{
		return _colCount;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch (columnIndex)
		{
		case id: return "Sys.id";
		case jePripravenaNaExpedovanie: return "Je pripravená na expedovanie";
		case jeOpracovana: return "Je opracovaná";
		case sklad: return "Sklad";
		case vozidlo: return "Vozidlo";
		case dopravnik: return "Dopravník";
		case cielovySklad: return "Cieľový sklad";
		}
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		try
		{
			Rolka rolka = _datasource.rolky().get(rowIndex).rolka();
			switch (columnIndex)
			{
			case id: return rolka.id();
			case jePripravenaNaExpedovanie: return rolka.jePripravenaNaExpedovanie() ? "Ano" : "Nie";
			case jeOpracovana: return rolka.jeOpracovana() ? "Opracovaná" : "Neopracoovaná";
			case sklad: return rolka.sklad() != null ? rolka.sklad() : "-";
			case vozidlo: return rolka.vozidlo() != null ? rolka.vozidlo() : "-";
			case dopravnik: return rolka.dopravnik() != null ? rolka.dopravnik() : "-";
			case cielovySklad: return rolka.cielovySklad();
			}
		}
		catch (RuntimeException ex) {}
		return null;
	}

}
