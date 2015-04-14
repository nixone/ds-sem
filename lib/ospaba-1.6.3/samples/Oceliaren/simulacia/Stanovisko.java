package simulacia;

public enum Stanovisko
{
	S1(0, Id.zeriav1),
	S2(1, Id.zeriav2),
	S3(2, Id.zeriav3);
	
	private int _index;
	private int _idZeriavu;
	private int [][] _maticaVzdialenosti = // [m]
	{
		{    0,  400, 1140 },
		{ 1520,    0,  740 },
		{  780, 1180,    0 }
	};
	
	private Stanovisko(int index, int idZeriavu)
	{
		_index = index;
		_idZeriavu = idZeriavu;
	}
	
	public double vzdialenost(Stanovisko ciel)
	{
		return _maticaVzdialenosti[this._index][ciel._index];
	}
	
	public int index()
	{ return _index; }
	
	public int idZeriavu()
	{ return _idZeriavu; }
	
	public String toString()
	{
		switch (this)
		{
		case S1: return "St.1";
		case S2: return "St.2";
		case S3: return "St.3";
		}
		return null;
	}
}
