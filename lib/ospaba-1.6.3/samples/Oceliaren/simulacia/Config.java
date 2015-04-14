package simulacia;

public class Config
{
	public static int pocetStarychVozidiel = 3;
	public static int pocetNovychVozidiel = 1;
	public static int pocetTimov = 3;
			
	public static int trvanieZahrievania = 288000;
	public static int trvanieReplikacie = 525600 + trvanieZahrievania;
	public static int pocetReplikacii = 20;
	
	public static int [] poctyOpracovanychRoliekVSkladoch = { 36, 14, 25 ,30 };
	public static int [] poctyNeopracovanychRoliekVSkladoch = { 24, 22, 20, 56 };
	
	public static double intenzitaVstupnehoToku = .9;

	public static final int pocetZeriavov = 3;
	public static final int pocetSkladov = 4;
	public static final int pocetDopravnikov = 3;
	
	private Config() {};
}
