package sk.nixone.ds.core;

public class NumberUtil {
	static public int readBig(String input) {
		if(input.toLowerCase().endsWith("m")) {
			return Integer.parseInt(input.substring(0, input.length()-1))*1000000;
		} else if(input.toLowerCase().endsWith("k")) {
			return Integer.parseInt(input.substring(0, input.length()-1))*1000;
		}
		return Integer.parseInt(input);
	}
}
