package simulacia;

import OSPABA.IdList;

public class Mc extends IdList
{
	public static final int init = 1;
	public static final int initRolkaSkladu = 2;

	// agentOkolia
	public static final int prichodRolky = 101;
	
	// agentModelu
	public static final int spracovanieRolky = 201;
	public static final int spracovanieRolkyDokoncene = 202;
	public static final int odchodRolky = 203;
	
	// agentOceliarne -> agentDopravniku
	public static final int pridanieRolkyDoDopravnika = 301;
	public static final int pridanieRolkyDoDopravnikaDokoncene = 302;
	public static final int odstranenieRolkyZDopravnika = 303;
	public static final int odstranenieRolkyZDopravnikaDokoncene = 304;
	public static final int odvezenieRolky = 305;
	
	// agentOceliarne -> agentSkladov
	public static final int opracovanieRolky = 401;
	public static final int opracovanieRolkyDokoncene = 402;
	public static final int pridelenieMiestaVSklade = 403;
	public static final int pridelenieMiestaVSkladeDokoncene = 404;
	public static final int odstranenieRolkyZoSkladu = 405;
	
	// agentOceliarne -> agentVozidiel
	public static final int pridelenieVozidla = 501;
	public static final int pridelenieVozidlaDokoncene = 502;
	public static final int presunVozidla = 503;
	public static final int presunVozidlaDokonceny = 504;
	public static final int uvolnenieVozidla = 505;

	// agentOceliarne -> agentZeriavov
	public static final int pridelenieZeriavu = 601;
	public static final int pridelenieZeriavuDokoncene = 602;
	public static final int prelozenieRolky = 603;
	public static final int prelozenieRolkyDokoncene = 604;
	public static final int uvolnenieZeriavu = 605;
	
	// kontinualnyAsistenti
	public static final int hold = 10001;
}
