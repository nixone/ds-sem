package simulacia;

public class Config
{
	public static double simStartTime = 0d;

	public static final double casOtvoreniaRestauracie = simStartTime + 2 * 3600d; // pred tymto casom do restauracie zakaznici nevstupuju
	public static final double casZatvoreniaRestauracie = casOtvoreniaRestauracie + 9 * 3600d; // po tomto case uz neprichadzaju novy zakaznici
	
	public static final double chladenie = 1.5 * 3600d;
	public static final double zahrievanie = casOtvoreniaRestauracie - 10 * 60d;
	public static double casOdKtorehoZakazniciCakajuPredRestauraciou = casOtvoreniaRestauracie + zahrievanie;
	
	public static double simEndTime = casZatvoreniaRestauracie + chladenie;
	
	public static double trvanieReplikacie = Math.abs(simStartTime) + Math.abs(simEndTime);
	public static double trvanieObsluhyVRestauracii = casZatvoreniaRestauracie - casOtvoreniaRestauracie;	
}
