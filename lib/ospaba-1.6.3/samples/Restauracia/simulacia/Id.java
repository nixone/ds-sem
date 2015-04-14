package simulacia;

import OSPABA.IdList;

public class Id extends IdList
{
	// agenti
	public static final int agentModelu = 1;
	public static final int agentOkolia = 2;
	public static final int agentRestovracie = 3;
	public static final int agentKuchyne = 4;
	public static final int agentJedalne = 5;

	// manazeri
	public static final int manazerModelu = 101;
	public static final int manazerOkolia = 102;
	public static final int manazerRestovracie = 103;
	public static final int manazerKuchyne = 104;
	public static final int manazerJedalne = 105;
	
	// kontinualny asistenti
	public static final int planovacPrichoduZakaznikov = 1001;
	public static final int procesObjednavania = 1002;
	public static final int procesPrineseniaJedla = 1003;
	public static final int procesPripravyJedla = 1004;
	public static final int procesJedenia = 1005;
	public static final int procesPlatenia = 1006;
	public static final int planovacOtvoreniaRestauracie = 1007;
	
	// promptny asistenti
	public static final int dotazNajkratsiePracujuciKuchar = 2001;
	public static final int dotazNajkratsiePracujuciCasnik = 2002;
	public static final int akciaPriradStol = 2003;
}
