package entity;

import simulacia.SimulaciaRestovracie;
import OSPABA.Entity;
import OSPABA.Simulation;
import OSPExceptions.SimException;
import OSPRNG.RNGTemplate;
import OSPRNG.UniformContinuousRNG;

public class Zakaznik extends Entity
{
	private static RNGTemplate _rngTemplate = new RNGTemplate(UniformContinuousRNG.class, 0d, 1d);

	private UniformContinuousRNG _uniform;

	private double _zaciatokCakania;
	private double _casCakania;
	private boolean _caka;
	
	public Zakaznik(Simulation mySim)
	{
		super(mySim);
		
		_uniform = (UniformContinuousRNG)_rngTemplate.generator(((SimulaciaRestovracie)mySim).id());
		
		_zaciatokCakania = .0;
		_casCakania = .0;
		_caka = false;
	}

	public Pokrm vyberPokrm()
	{
		double p = _uniform.sample();
		
		if (p < .2) return Pokrm.A;
		if (p < .65) return Pokrm.B;
		if (p < .95) return Pokrm.C;
		if (p < 1) return Pokrm.D;

		throw new SimException("Problem s generatorom vyberu pokrmou");
	}
	
	public void zaciatokCakania()
	{
		if (_caka == false)
		{
			_caka = true;
			_zaciatokCakania = mySim().currentTime();
		}
		else throw new SimException("Cakanie musi skoncit pred tim ako moze znovu zacat");
	}
	
	public void koniecCakania()
	{
		if (_caka == true)
		{
			_caka = false;
			_casCakania += mySim().currentTime() - _zaciatokCakania;
		}
		else throw new SimException("Cakanie musi zacat pred tym ako moze skoncit");
	}
	
	public double casCakania()
	{ return _casCakania; }
	
	public double casZaciatkuCakania()
	{ return _zaciatokCakania; }
	
	public boolean caka()
	{ return _caka; }
}
