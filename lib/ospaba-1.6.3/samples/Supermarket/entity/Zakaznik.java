package supermarket.entity;

import OSPABA.Entity;
import OSPABA.Simulation;
import OSPRNG.EmpiricRNG;
import OSPRNG.EmpiricPair;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;

public class Zakaznik extends Entity
{
	private static UniformContinuousRNG _rand = new UniformContinuousRNG(0d, 1d);

	private final static double kPercentoZelenina = .17;
	private final static double kPercentoMasoSyr = .11;

	private static EmpiricRNG _empiric = new EmpiricRNG(
			new EmpiricPair(new UniformDiscreteRNG(1, 5), .1),
			new EmpiricPair(new UniformDiscreteRNG(6, 10), .2),
			new EmpiricPair(new UniformDiscreteRNG(11, 15), .22),
			new EmpiricPair(new UniformDiscreteRNG(16, 20), .16),
			new EmpiricPair(new UniformDiscreteRNG(21, 25), .12),
			new EmpiricPair(new UniformDiscreteRNG(26, 30), .11),
			new EmpiricPair(new UniformDiscreteRNG(31, 100), .09)
		);

	private final int _pocetTovarov;
	private int _pocetZelenina;
	private int _pocetMasoSyr;
	
	private double _casZaradeniaDoFrontu;
	private double _casCasUkonceniaObsluhy;
	
	private double _celkovyCasCakania;
	
	public Zakaznik(Simulation sim)
	{
		super(sim);
		
		_pocetTovarov = _empiric.sample().intValue();
		_pocetZelenina = 0;
		_pocetMasoSyr = 0;
		for (int i = 0; i < _pocetTovarov; ++i)
		{
			double rand = _rand.sample();
			if (rand < kPercentoZelenina)
			{
				++_pocetZelenina;
			}
			else if (rand < kPercentoZelenina + kPercentoMasoSyr)
			{
				++_pocetMasoSyr;
			}
		}
		
		_casZaradeniaDoFrontu = -1;
		_casCasUkonceniaObsluhy = -1;
		
		_celkovyCasCakania = .0; 
	}
	
	public int pocetTovarov()
	{ return _pocetTovarov; }

	public int pocetOstatnychTovarov()
	{
		return pocetTovarov() - pocetZelenina() - pocetMasoASyr();
	}
	
	public int pocetZelenina()
	{ return _pocetZelenina; }
	
	public int pocetMasoASyr()
	{ return _pocetMasoSyr; }
	
	public double casZaradeniaDoFrontu()
	{ return _casZaradeniaDoFrontu; }
	
	public void setCasZaradeniaDoFrontu(double casZaradeniaDoFrontu)
	{ _casZaradeniaDoFrontu = casZaradeniaDoFrontu; }

	public double casCasUkonceniaObsluhy()
	{ return _casCasUkonceniaObsluhy; }

	public void setCasCasUkonceniaObsluhy(double casCasUkonceniaObsluhy)
	{ _casCasUkonceniaObsluhy = casCasUkonceniaObsluhy; }
	
	public double celkovyCasCakania()
	{ return _celkovyCasCakania; }

	public void zvisCelkovyCasCakania(double trvanie)
	{
		_celkovyCasCakania += trvanie;
	}
}

