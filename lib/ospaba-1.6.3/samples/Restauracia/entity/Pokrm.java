package entity;

import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.RNG;
import OSPRNG.UniformDiscreteRNG;

public enum Pokrm
{
	A (0, new UniformDiscreteRNG(380, 440)),
	B (1, new EmpiricRNG(
			new EmpiricPair(new UniformDiscreteRNG(190, 345), .05),
			new EmpiricPair(new UniformDiscreteRNG(346, 630), .65),
			new EmpiricPair(new UniformDiscreteRNG(631, 950), .3)
		)),
	C (2, new EmpiricRNG(
			new EmpiricPair(new UniformDiscreteRNG(290, 356), .35),
			new EmpiricPair(new UniformDiscreteRNG(357, 540), .65)
		)),
	D (3, new RNG<Integer>() {
			@Override
			public Integer sample()
			{ return 180; }	
		});

	public final int index;
	private RNG generator;
	
	private Pokrm(int index, RNG generator)
	{
		this.index = index;
		this.generator = generator;
	}
	
	public double casPripravy()
	{
		return generator.sample().doubleValue();
	}
}
