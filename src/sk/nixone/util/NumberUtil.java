package sk.nixone.util;

/**
 * Pomocne nastroje pre pracu s cislami.
 * 
 * @author nixone
 */
public class NumberUtil {
	
	/**
	 * Konvertuje jednoduchy zapis velkeho cisla (obycajne z uzivatelskeho vstupu) na cele cislo zrozumitelne pre Javu.
	 * Konverzia prebieha na zaklade zakladu cisla a jedneho znaku ako pripony.
	 * 
	 * Napriklad "100m" konvertuje na 100 * 1 000 000 = 100 000 000,
	 * alebo "5k" konvertuje na 5 * 1 000 = 5 000
	 * 
	 * @param input zapis velkeho cisla
	 * @return velke cislo
	 */
	static public int readBig(String input) {
		if(input.toLowerCase().endsWith("m")) {
			return Integer.parseInt(input.substring(0, input.length()-1))*1000000;
		} else if(input.toLowerCase().endsWith("k")) {
			return Integer.parseInt(input.substring(0, input.length()-1))*1000;
		}
		return Integer.parseInt(input);
	}
	
	static public String nicePrice(int price) {
		String output = "";
		while(price > 1000) {
			int remainder = price % 1000;
			price /= 1000;
			output = String.format("%03d", remainder)+" "+output;
		}
		output = price+" "+output;
		return output;
	}
}
