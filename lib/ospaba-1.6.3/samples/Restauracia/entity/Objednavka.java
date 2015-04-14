package entity;

import java.util.ArrayList;
import java.util.List;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Objednavka extends Entity
{
	private List< Pokrm > _pokrmy;
	private int _pripravovane;
	private int _hotove;
	private SkupinaZakaznikov _objednavajuci;

	public Objednavka(Simulation mySim, SkupinaZakaznikov objednavajuci)
	{
		super(mySim);
		_pokrmy = new ArrayList<>();
		_objednavajuci = objednavajuci;
		_pripravovane = 0;
		_hotove = 0;
		
		for (Zakaznik zakaznik : objednavajuci.zakaznici())
		{
			_pokrmy.add(zakaznik.vyberPokrm());
		}
	}

	public void pridajPokrm(Pokrm pokrm)
	{
		_pokrmy.add(pokrm);
	}
	
	public boolean maDalsiPokrm()
	{
		return _pripravovane < _pokrmy.size();
	}
	
	public Pokrm dalsiPokrm()
	{
		return _pokrmy.get(_pripravovane++);
	}
	
	public void pokrmPripraveny()
	{
		++_hotove;
	}
	
	public boolean jeVsetkoPripravene()
	{
		return _hotove == _pokrmy.size();
	}
	
	public int pocetPripravovanychPokrmov()
	{ return _pripravovane - _hotove; }
	
	public int pocetPripravenychPokrmov()
	{ return _hotove; }
	
	public int pocetNepripravenychPokrmov()
	{ return _pokrmy.size() - _hotove; }
	
	public List< Pokrm > pokrmy()
	{ return _pokrmy; }

	public SkupinaZakaznikov objednavajuci()
	{ return _objednavajuci; }
}
