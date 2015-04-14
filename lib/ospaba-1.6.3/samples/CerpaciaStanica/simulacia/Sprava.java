package simulacia;

import OSPABA.MessageForm;
import OSPABA.Simulation;

public class Sprava extends MessageForm
{
	private double _zaciatokCakania;
	private double _celkoveCakanie;
	private int _x;
	
	public Sprava(Simulation sim)
	{
		super(sim);
		_zaciatokCakania = .0;
		_celkoveCakanie = .0;
	}

	private Sprava(Sprava original)
	{
		super(original);
		_zaciatokCakania = ((Sprava)original)._zaciatokCakania;
		_celkoveCakanie = ((Sprava)original)._celkoveCakanie;
	}
	
	@Override
	public Sprava createCopy()
	{
		return new Sprava(this);
	}
	
	public double zaciatokCakania()
	{ return _zaciatokCakania; }
	
	public void setZaciatokCakania(double zaciatokCakania)
	{ _zaciatokCakania = zaciatokCakania; }
	
	public double celkoveCakanie()
	{ return _celkoveCakanie; }
	
	public void setCelkoveCakanie(double celkoveCakanie)
	{ _celkoveCakanie = celkoveCakanie; }

	public int x()
	{ return _x; }

	public void setX(int x)
	{ _x = x; }
}
